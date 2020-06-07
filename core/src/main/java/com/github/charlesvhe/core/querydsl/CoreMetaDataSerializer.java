package com.github.charlesvhe.core.querydsl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.TypeMappings;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.codegen.MetaDataSerializer;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.SQLCodegenModule;
import com.querydsl.sql.codegen.support.ForeignKeyData;
import com.querydsl.sql.codegen.support.InverseForeignKeyData;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CoreMetaDataSerializer extends MetaDataSerializer {
    private final NamingStrategy namingStrategy;
    private final Class<?> entityPathType;

    /**
     * Create a new {@code MetaDataSerializer} instance
     *
     * @param typeMappings
     * @param namingStrategy      naming strategy for table to class and column to property conversion
     * @param innerClassesForKeys wrap key properties into inner classes (default: false)
     * @param imports             java user imports
     * @param columnComparator
     * @param entityPathType
     */
    @Inject
    public CoreMetaDataSerializer(TypeMappings typeMappings,
                                  NamingStrategy namingStrategy,
                                  @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys,
                                  @Named(SQLCodegenModule.IMPORTS) Set<String> imports,
                                  @Named(SQLCodegenModule.COLUMN_COMPARATOR) Comparator<Property> columnComparator,
                                  @Named(SQLCodegenModule.ENTITYPATH_TYPE) Class<?> entityPathType) {
        super(typeMappings, namingStrategy, innerClassesForKeys, imports, columnComparator, entityPathType);
        this.namingStrategy = namingStrategy;
        this.entityPathType = entityPathType;
    }

    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config,
                                EntityType model) throws IOException {
        //from EntitySerializer.introImports()

        writer.staticimports(PathMetadataFactory.class);

        // import package of query type
        Type queryType = typeMappings.getPathType(model, model, true);
        if (!model.getPackageName().isEmpty()
                && !queryType.getPackageName().equals(model.getPackageName())
                && !queryType.getSimpleName().equals(model.getSimpleName())) {
            String fullName = model.getFullName();
            String packageName = model.getPackageName();
            if (fullName.substring(packageName.length() + 1).contains(".")) {
                fullName = fullName.substring(0, fullName.lastIndexOf('.'));
            }
            writer.importClasses(fullName);
        }

        // delegate packages
        introDelegatePackages(writer, model);

        // other packages
        writer.imports(SimpleExpression.class.getPackage());

        // other classes
        List<Class<?>> classes = Lists.<Class<?>>newArrayList(PathMetadata.class);
        if (!getUsedClassNames(model).contains("Path")) {
            classes.add(Path.class);
        }
        if (!model.getConstructors().isEmpty()) {
            classes.add(ConstructorExpression.class);
            classes.add(Projections.class);
            classes.add(Expression.class);
        }
        boolean inits = false;
        if (model.hasEntityFields() || model.hasInits()) {
            inits = true;
        } else {
            Set<TypeCategory> collections = Sets.newHashSet(TypeCategory.COLLECTION, TypeCategory.LIST, TypeCategory.SET);
            for (Property property : model.getProperties()) {
                if (!property.isInherited() && collections.contains(property.getType().getCategory())) {
                    inits = true;
                    break;
                }
            }
        }
        if (inits) {
            classes.add(PathInits.class);
        }
        writer.imports(classes.toArray(new Class<?>[classes.size()]));

        // from MetaDataSerializer.introImports()
        Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>)
                model.getData().get(ForeignKeyData.class);
        Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>)
                model.getData().get(InverseForeignKeyData.class);
        boolean addJavaUtilImport = false;
        if (foreignKeys != null) {
            for (ForeignKeyData keyData : foreignKeys) {
                if (keyData.getForeignColumns().size() > 1) {
                    addJavaUtilImport = true;
                }
            }
        }
        if (inverseForeignKeys != null) {
            for (InverseForeignKeyData keyData : inverseForeignKeys) {
                if (keyData.getForeignColumns().size() > 1) {
                    addJavaUtilImport = true;
                }
            }
        }

        if (addJavaUtilImport) {
            writer.imports(List.class.getPackage());
        }

        writer.imports(ColumnMetadata.class, java.sql.Types.class);

        if (!entityPathType.getPackage().equals(ColumnMetadata.class.getPackage())) {
            writer.imports(entityPathType);
        }

        writeUserImports(writer);
    }

    private Set<String> getUsedClassNames(EntityType model) {
        Set<String> result = Sets.newHashSet();
        result.add(model.getSimpleName());
        for (Property property : model.getProperties()) {
            result.add(property.getType().getSimpleName());
            for (Type type : property.getType().getParameters()) {
                if (type != null) {
                    result.add(type.getSimpleName());
                }
            }
        }
        return result;
    }

    @Override
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        TypeCategory category = model.getOriginalCategory();
        // serialize annotations only, if no bean types are used
        if (model.equals(queryType)) {
            for (Annotation annotation : model.getAnnotations()) {
                writer.annotation(annotation);
            }
        }
        writer.beginClass(queryType, new ClassType(category, entityPathType, model));
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
    }

    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType, String defaultName) throws IOException {
        String variableName = !defaultName.isEmpty() ? defaultName : namingStrategy.getDefaultVariableName(entityType);
        String alias = namingStrategy.getDefaultAlias(entityType);
        Type queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, "q" + queryType.getSimpleName().substring(1), "new " + queryType.getSimpleName() + "(\"" + alias + "\")");
    }
}

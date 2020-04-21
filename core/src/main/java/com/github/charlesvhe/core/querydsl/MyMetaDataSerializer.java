package com.github.charlesvhe.core.querydsl;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Type;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.TypeMappings;
import com.querydsl.sql.codegen.MetaDataSerializer;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.SQLCodegenModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

public class MyMetaDataSerializer extends MetaDataSerializer {
    private NamingStrategy namingStrategy;

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
    public MyMetaDataSerializer(TypeMappings typeMappings,
                                NamingStrategy namingStrategy,
                                @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys,
                                @Named(SQLCodegenModule.IMPORTS) Set<String> imports,
                                @Named(SQLCodegenModule.COLUMN_COMPARATOR) Comparator<Property> columnComparator,
                                @Named(SQLCodegenModule.ENTITYPATH_TYPE) Class<?> entityPathType) {
        super(typeMappings, namingStrategy, innerClassesForKeys, imports, columnComparator, entityPathType);
        this.namingStrategy = namingStrategy;
    }


    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType, String defaultName) throws IOException {
        String variableName = !defaultName.isEmpty() ? defaultName : namingStrategy.getDefaultVariableName(entityType);
        String alias = namingStrategy.getDefaultAlias(entityType);
        Type queryType = typeMappings.getPathType(entityType, entityType, true);
        writer.publicStaticFinal(queryType, "q" + queryType.getSimpleName().substring(1), "new " + queryType.getSimpleName() + "(\"" + alias + "\")");
    }
}

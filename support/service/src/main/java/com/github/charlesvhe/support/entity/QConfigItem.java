package com.github.charlesvhe.support.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QConfigItem is a Querydsl query type for ConfigItem
 */
@Generated("com.github.charlesvhe.core.querydsl.MyMetaDataSerializer")
public class QConfigItem extends com.querydsl.sql.RelationalPathBase<ConfigItem> {

    private static final long serialVersionUID = -1136621664;

    public static final QConfigItem qConfigItem = new QConfigItem("CONFIG_ITEM");

    public final StringPath appId = createString("appId");

    public final DateTimePath<java.util.Date> datetime1 = createDateTime("datetime1", java.util.Date.class);

    public final DateTimePath<java.util.Date> datetime2 = createDateTime("datetime2", java.util.Date.class);

    public final DateTimePath<java.util.Date> datetime3 = createDateTime("datetime3", java.util.Date.class);

    public final NumberPath<java.math.BigDecimal> decimal1 = createNumber("decimal1", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> decimal2 = createNumber("decimal2", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> decimal3 = createNumber("decimal3", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> decimal4 = createNumber("decimal4", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> decimal5 = createNumber("decimal5", java.math.BigDecimal.class);

    public final DateTimePath<java.util.Date> gmtCreate = createDateTime("gmtCreate", java.util.Date.class);

    public final DateTimePath<java.util.Date> gmtModified = createDateTime("gmtModified", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath metaCode = createString("metaCode");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Short> sort = createNumber("sort", Short.class);

    public final StringPath text1 = createString("text1");

    public final StringPath text2 = createString("text2");

    public final StringPath text3 = createString("text3");

    public final StringPath varchar1 = createString("varchar1");

    public final StringPath varchar10 = createString("varchar10");

    public final StringPath varchar2 = createString("varchar2");

    public final StringPath varchar3 = createString("varchar3");

    public final StringPath varchar4 = createString("varchar4");

    public final StringPath varchar5 = createString("varchar5");

    public final StringPath varchar6 = createString("varchar6");

    public final StringPath varchar7 = createString("varchar7");

    public final StringPath varchar8 = createString("varchar8");

    public final StringPath varchar9 = createString("varchar9");

    public final com.querydsl.sql.PrimaryKey<ConfigItem> constraint83 = createPrimaryKey(id);

    public QConfigItem(String variable) {
        super(ConfigItem.class, forVariable(variable), "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(String variable, String schema, String table) {
        super(ConfigItem.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QConfigItem(String variable, String schema) {
        super(ConfigItem.class, forVariable(variable), schema, "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(Path<? extends ConfigItem> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(PathMetadata metadata) {
        super(ConfigItem.class, metadata, "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(appId, ColumnMetadata.named("APP_ID").withIndex(2).ofType(Types.VARCHAR).withSize(64).notNull());
        addMetadata(datetime1, ColumnMetadata.named("DATETIME1").withIndex(24).ofType(Types.TIMESTAMP).withSize(26).withDigits(6).notNull());
        addMetadata(datetime2, ColumnMetadata.named("DATETIME2").withIndex(25).ofType(Types.TIMESTAMP).withSize(26).withDigits(6).notNull());
        addMetadata(datetime3, ColumnMetadata.named("DATETIME3").withIndex(26).ofType(Types.TIMESTAMP).withSize(26).withDigits(6).notNull());
        addMetadata(decimal1, ColumnMetadata.named("DECIMAL1").withIndex(19).ofType(Types.DECIMAL).withSize(19).withDigits(4).notNull());
        addMetadata(decimal2, ColumnMetadata.named("DECIMAL2").withIndex(20).ofType(Types.DECIMAL).withSize(19).withDigits(4).notNull());
        addMetadata(decimal3, ColumnMetadata.named("DECIMAL3").withIndex(21).ofType(Types.DECIMAL).withSize(19).withDigits(4).notNull());
        addMetadata(decimal4, ColumnMetadata.named("DECIMAL4").withIndex(22).ofType(Types.DECIMAL).withSize(19).withDigits(4).notNull());
        addMetadata(decimal5, ColumnMetadata.named("DECIMAL5").withIndex(23).ofType(Types.DECIMAL).withSize(19).withDigits(4).notNull());
        addMetadata(gmtCreate, ColumnMetadata.named("GMT_CREATE").withIndex(27).ofType(Types.TIMESTAMP).withSize(26).withDigits(6).notNull());
        addMetadata(gmtModified, ColumnMetadata.named("GMT_MODIFIED").withIndex(28).ofType(Types.TIMESTAMP).withSize(26).withDigits(6).notNull());
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(metaCode, ColumnMetadata.named("META_CODE").withIndex(3).ofType(Types.VARCHAR).withSize(64).notNull());
        addMetadata(parentId, ColumnMetadata.named("PARENT_ID").withIndex(4).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(sort, ColumnMetadata.named("SORT").withIndex(5).ofType(Types.SMALLINT).withSize(5).notNull());
        addMetadata(text1, ColumnMetadata.named("TEXT1").withIndex(16).ofType(Types.CLOB).withSize(2147483647));
        addMetadata(text2, ColumnMetadata.named("TEXT2").withIndex(17).ofType(Types.CLOB).withSize(2147483647));
        addMetadata(text3, ColumnMetadata.named("TEXT3").withIndex(18).ofType(Types.CLOB).withSize(2147483647));
        addMetadata(varchar1, ColumnMetadata.named("VARCHAR1").withIndex(6).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar10, ColumnMetadata.named("VARCHAR10").withIndex(15).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar2, ColumnMetadata.named("VARCHAR2").withIndex(7).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar3, ColumnMetadata.named("VARCHAR3").withIndex(8).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar4, ColumnMetadata.named("VARCHAR4").withIndex(9).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar5, ColumnMetadata.named("VARCHAR5").withIndex(10).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar6, ColumnMetadata.named("VARCHAR6").withIndex(11).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar7, ColumnMetadata.named("VARCHAR7").withIndex(12).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar8, ColumnMetadata.named("VARCHAR8").withIndex(13).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(varchar9, ColumnMetadata.named("VARCHAR9").withIndex(14).ofType(Types.VARCHAR).withSize(512).notNull());
    }

}


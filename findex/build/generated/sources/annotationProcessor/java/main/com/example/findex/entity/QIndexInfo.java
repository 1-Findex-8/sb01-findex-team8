package com.example.findex.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIndexInfo is a Querydsl query type for IndexInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIndexInfo extends EntityPathBase<IndexInfo> {

    private static final long serialVersionUID = -1736638266L;

    public static final QIndexInfo indexInfo = new QIndexInfo("indexInfo");

    public final com.example.findex.entity.base.QBaseEntity _super = new com.example.findex.entity.base.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> baseIndex = createNumber("baseIndex", java.math.BigDecimal.class);

    public final DatePath<java.time.LocalDate> basePointInTime = createDate("basePointInTime", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> employeeItemsCount = createNumber("employeeItemsCount", Integer.class);

    public final BooleanPath favorite = createBoolean("favorite");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath indexClassification = createString("indexClassification");

    public final StringPath indexName = createString("indexName");

    public final EnumPath<SourceType> sourceType = createEnum("sourceType", SourceType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QIndexInfo(String variable) {
        super(IndexInfo.class, forVariable(variable));
    }

    public QIndexInfo(Path<? extends IndexInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIndexInfo(PathMetadata metadata) {
        super(IndexInfo.class, metadata);
    }

}


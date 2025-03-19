package com.example.findex.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAutoSyncConfigs is a Querydsl query type for AutoSyncConfigs
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAutoSyncConfigs extends EntityPathBase<AutoSyncConfigs> {

    private static final long serialVersionUID = -1051817779L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAutoSyncConfigs autoSyncConfigs = new QAutoSyncConfigs("autoSyncConfigs");

    public final com.example.findex.entity.base.QBaseEntity _super = new com.example.findex.entity.base.QBaseEntity(this);

    public final BooleanPath active = createBoolean("active");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QIndexInfo indexInfo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAutoSyncConfigs(String variable) {
        this(AutoSyncConfigs.class, forVariable(variable), INITS);
    }

    public QAutoSyncConfigs(Path<? extends AutoSyncConfigs> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAutoSyncConfigs(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAutoSyncConfigs(PathMetadata metadata, PathInits inits) {
        this(AutoSyncConfigs.class, metadata, inits);
    }

    public QAutoSyncConfigs(Class<? extends AutoSyncConfigs> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.indexInfo = inits.isInitialized("indexInfo") ? new QIndexInfo(forProperty("indexInfo")) : null;
    }

}


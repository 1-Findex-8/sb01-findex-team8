package com.example.findex.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSyncJobs is a Querydsl query type for SyncJobs
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSyncJobs extends EntityPathBase<SyncJobs> {

    private static final long serialVersionUID = -707374933L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSyncJobs syncJobs = new QSyncJobs("syncJobs");

    public final com.example.findex.entity.base.QBaseEntity _super = new com.example.findex.entity.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QIndexInfo indexInfo;

    public final DateTimePath<java.time.LocalDateTime> jobTime = createDateTime("jobTime", java.time.LocalDateTime.class);

    public final EnumPath<JobType> jobType = createEnum("jobType", JobType.class);

    public final EnumPath<Result> result = createEnum("result", Result.class);

    public final DatePath<java.time.LocalDate> targetDate = createDate("targetDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath worker = createString("worker");

    public QSyncJobs(String variable) {
        this(SyncJobs.class, forVariable(variable), INITS);
    }

    public QSyncJobs(Path<? extends SyncJobs> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSyncJobs(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSyncJobs(PathMetadata metadata, PathInits inits) {
        this(SyncJobs.class, metadata, inits);
    }

    public QSyncJobs(Class<? extends SyncJobs> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.indexInfo = inits.isInitialized("indexInfo") ? new QIndexInfo(forProperty("indexInfo")) : null;
    }

}


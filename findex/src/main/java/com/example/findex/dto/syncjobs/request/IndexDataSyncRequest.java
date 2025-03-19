package com.example.findex.dto.syncjobs.request;

import java.time.LocalDate;
import java.util.List;

public record IndexDataSyncRequest(
    List<Long> indexInfoIds,
    LocalDate baseDateFrom,
    LocalDate baseDateTo
) {

}

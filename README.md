<div style="text-align: center;">
    <img src="https://github.com/user-attachments/assets/b41c8f05-fbfd-45ca-9506-19d3ab9fd71f" style="width: 100%; max-width: 1000px; height: auto;">
</div>

# :chart_with_upwards_trend: Findex

금융 지수 데이터를 종합적으로 제공하는 대시보드 서비스

---

# :family: 팀명 및 팀원 구성
### 팀명
sb01-findex-team8
(https://github.com/1-Findex-8/sb01-findex-team8)

### 팀원 구성
- 김주언 (https://github.com/wndjs803)
- 이유빈 (https://github.com/iiyubb)
- 손동혁 (https://github.com/sondonghyuk)
- 장태준 (https://github.com/janghoosa)
---

### :tv: 발표자료
[8팀 발표자료.pdf](https://github.com/user-attachments/files/19412259/8.pdf)


---

## :pushpin: 목차
1. [프로젝트 소개](#프로젝트-소개)
2. [기술 스택](#기술-스택)
3. [팀원별 구현 기능](#팀원별-구현-기능)
4. [파일 구조](#파일-구조)


---
### :information_desk_person: 프로젝트 소개

프로젝트명: 코드잇 스프린트 Spring 백엔트 트랙 초급 프로젝트 Findex


프로젝트 기간: 2025.03.14 ~ 2025.03.23

---
### :computer: 기술 스택

- **Backend**
  - Spring Boot: 애플리케이션 개발을 위한 핵심 프레임워크
  - Spring Data JPA: 데이터베이스 연동을 위한 JPA 사용
  - springdoc-openapi: API 문서화를 위한 라이브러리
  - MapStruct: 객체 변환을 위한 라이브러리
  - Railway.io: CI/CD 배포 관리 툴
- **Database**
  - PostgreSQL : 데이터베이스
- **기타 툴**
  - Git & GitHub: 버전 관리 및 협업 툴
  - Discord: 팀원 간 커뮤니케이션
  - Notion: 프로젝트 관리 및 문서화

---
### :mag_right: 팀원별 구현 기능

#### 김주언
- **연동 작업 API**
  - 지수 정보 연동  `POST /api/sync-jobs/index-infos`
  - 지수 데이터 연동  `POST /api/sync-jobs/index-data`
  - 연동 작업 목록 조회 `GET /api/sync-jobs`

- **자동 연동 설정 API**
  - 자동 연동 설정 수정 `PATCH /api/auto-sync-configs/{id}`
  - 자동 연동 설정 목록 조회 `GET /api/auto-sync-configs`

#### 이유빈
- **지수 정보 관리 API**
  - 지수 정보 목록 조회 `GET /api/index-infos`
  - 지수 정보 등록 `POST /api/index-infos`
  - 지수 정보 조회 `GET /api/index-infos/{id}`
  - 지수 정보 삭제 `DELETE /api/index-infos/{id}`
  - 지수 정보 수정 `PATCH /api/index-infos/{id}`
  - 지수 정보 요약 목록 조회 `GET /api/index-infos/summaries`

#### 손동혁
- **지수 데이터 관리 API**
  - 지수 데이터 목록 조회 `GET /api/index-data`
  - 지수 데이터 등록 `POST /api/index-data`
  - 지수 데이터 삭제 `DELETE /api/index-data/{id}`
  - 지수 데이터 수정 `PATCH /api/index-data/{id}`

#### 장태준
- **지수 성과 관련 API**
  - 지수 차트 조회 `GET /api/index-data/{id}/chart`
  - 지수 성과 랭킹 조회 `GET /api/index-data/performance/rank`
  - 관심 지수 성과 조회 `GET /api/index-data/performance/favorite`
  - 지수 데이터 CSV export `GET /api/index-data/export/csv`


---

### :file_folder: 파일 구조
```
   findex
   ├─ src
   │  └─ main
   │     ├─ java
   │     │  └─ com
   │     │     └─ example
   │     │        └─ findex
   │     │           ├─ FindexApplication.java
   │     │           ├─ api
   │     │           │  ├─ AutoSyncConfigApi.java
   │     │           │  ├─ IndexDataApi.java
   │     │           │  └─ SyncJobApi.java
   │     │           ├─ controller
   │     │           │  ├─ AutoSyncConfigController.java
   │     │           │  ├─ IndexDataController.java
   │     │           │  ├─ IndexInfoController.java
   │     │           │  └─ SyncJobsController.java
   │     │           ├─ dto
   │     │           │  ├─ autosyncconfigs
   │     │           │  │  ├─ request
   │     │           │  │  │  └─ AutoSyncConfigsUpdatedRequest.java
   │     │           │  │  └─ response
   │     │           │  │     ├─ AutoSyncConfigsDto.java
   │     │           │  │     └─ CursorPageResponseAutoSyncConfigDto.java
   │     │           │  ├─ indexdata
   │     │           │  │  ├─ data
   │     │           │  │  │  └─ IndexDataDto.java
   │     │           │  │  ├─ request
   │     │           │  │  │  ├─ IndexDataCreateRequest.java
   │     │           │  │  │  └─ IndexDataUpdateRequest.java
   │     │           │  │  └─ response
   │     │           │  │     ├─ ChartDataPoint.java
   │     │           │  │     ├─ CursorPageResponseIndexDataDto.java
   │     │           │  │     ├─ ErrorResponse.java
   │     │           │  │     ├─ IndexChartDto.java
   │     │           │  │     ├─ IndexPerformanceDto.java
   │     │           │  │     └─ RankedIndexPerformanceDto.java
   │     │           │  ├─ indexinfo
   │     │           │  │  ├─ CreateIndexInfoRequest.java
   │     │           │  │  ├─ CursorPageResponseIndexInfoDto.java
   │     │           │  │  ├─ FindIndexInfoRequest.java
   │     │           │  │  ├─ IndexInfoDto.java
   │     │           │  │  ├─ IndexInfoSummaryDto.java
   │     │           │  │  ├─ SortDirectionType.java
   │     │           │  │  └─ UpdateIndexInfoRequest.java
   │     │           │  └─ syncjobs
   │     │           │     ├─ request
   │     │           │     │  └─ IndexDataSyncRequest.java
   │     │           │     └─ response
   │     │           │        ├─ Body.java
   │     │           │        ├─ CursorPageResponseSyncJobDto.java
   │     │           │        ├─ GetStockMarketIndexResponse.java
   │     │           │        ├─ Header.java
   │     │           │        ├─ Item.java
   │     │           │        ├─ Items.java
   │     │           │        ├─ Response.java
   │     │           │        └─ SyncJobsDto.java
   │     │           ├─ entity
   │     │           │  ├─ AutoSyncConfigs.java
   │     │           │  ├─ IndexData.java
   │     │           │  ├─ IndexInfo.java
   │     │           │  ├─ JobType.java
   │     │           │  ├─ Result.java
   │     │           │  ├─ SourceType.java
   │     │           │  ├─ SyncJobs.java
   │     │           │  └─ base
   │     │           │     └─ BaseEntity.java
   │     │           ├─ global
   │     │           │  ├─ config
   │     │           │  │  ├─ QueryDslConfig.java
   │     │           │  │  ├─ RestTemplateConfig.java
   │     │           │  │  └─ SwaggerConfig.java
   │     │           │  └─ error
   │     │           │     ├─ ErrorCode.java
   │     │           │     ├─ ErrorResponse.java
   │     │           │     └─ exception
   │     │           │        ├─ BusinessException.java
   │     │           │        ├─ GlobalExceptionHandler.java
   │     │           │        ├─ autosyncconfigs
   │     │           │        │  └─ AutoSyncConfigNotFoundException.java
   │     │           │        ├─ indexdata
   │     │           │        │  ├─ IndexDataBadRequestException.java
   │     │           │        │  ├─ IndexDataIntegrityViolationException.java
   │     │           │        │  ├─ IndexDataInternalServerErrorException.java
   │     │           │        │  └─ IndexDataNoSuchElementException.java
   │     │           │        └─ indexinfo
   │     │           │           ├─ IndexInfoDuplicateException.java
   │     │           │           ├─ IndexInfoInvalidCursorException.java
   │     │           │           ├─ IndexInfoInvalidSortFieldException.java
   │     │           │           └─ IndexInfoNotFoundException.java
   │     │           ├─ mapper
   │     │           │  ├─ AutoSyncConfigsMapper.java
   │     │           │  ├─ IndexDataMapper.java
   │     │           │  ├─ IndexInfoMapper.java
   │     │           │  └─ SyncJobsMapper.java
   │     │           ├─ repository
   │     │           │  ├─ IndexDataRepository.java
   │     │           │  ├─ IndexDataRepositoryCustom.java
   │     │           │  ├─ IndexDataRepositoryImpl.java
   │     │           │  ├─ IndexInfoRepository.java
   │     │           │  ├─ autosyncconfigs
   │     │           │  │  ├─ AutoSyncConfigsCustomRepository.java
   │     │           │  │  ├─ AutoSyncConfigsCustomRepositoryImpl.java
   │     │           │  │  └─ AutoSyncConfigsRepository.java
   │     │           │  └─ syncjob
   │     │           │     ├─ SyncJobRepository.java
   │     │           │     ├─ SyncJobsCustomRepository.java
   │     │           │     └─ SyncJobsCustomRepositoryImpl.java
   │     │           └─ service
   │     │              ├─ AutoSyncConfigsService.java
   │     │              ├─ IndexDataService.java
   │     │              ├─ IndexInfoService.java
   │     │              └─ SyncJobsService.java
   │     └─ resources
   │       ├─ application-dev.yml
   │        ├─ application.yml
   │        └─ static
   │           ├─ assets
   │           │  ├─ index-CMh_iLGU.js
   │           │  └─ index-Dtn62Xmo.css
   │           ├─ favicon.ico
   │           └─ index.html
   ├─ .gitattributes
   ├─ .gitignore
   ├─ build.gradle
   ├─ checkstyle.xml
   ├─ gradle   
   ├─ gradlew
   ├─ gradlew.bat
   └─ settings.gradle


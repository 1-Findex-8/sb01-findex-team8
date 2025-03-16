package com.example.findex.index_data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.entity.base.BaseEntity;
import com.example.findex.mapper.IndexDataMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import com.example.findex.service.IndexDataServiceImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CreateIndexDataTest {

  @Mock // 가짜 객체 생성
  private IndexDataRepository indexDataRepository;

  @Mock
  private IndexInfoRepository indexInfoRepository;

  @Mock
  private IndexDataMapper indexDataMapper;

  @InjectMocks // Mock 객체들이 자동 주입
  private IndexDataServiceImpl indexDataService;  // 실제 서비스 클래스

  //IndexData 생성 성공 테스트
  @Test
  public void testCreateIndexData_Success()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    // Given
    Long indexInfoId = 1L;
    LocalDate baseDate = LocalDate.parse("2025-03-14");
    IndexDataCreateRequest request = new IndexDataCreateRequest(
        indexInfoId,
        baseDate,
        new BigDecimal("100.0"),
        new BigDecimal("95.0"),
        new BigDecimal("102.0"),
        new BigDecimal("98.0"),
        new BigDecimal("1.0"),
        new BigDecimal("1.05"),
        500L,
        1000L,
        1000000L
    );

    // Reflection을 사용하여 IndexInfo 객체 생성
    Constructor<IndexInfo> constructor = IndexInfo.class.getDeclaredConstructor();
    constructor.setAccessible(true);  // protected 생성자를 접근 가능하도록 설정
    IndexInfo indexInfo = constructor.newInstance();

    // 필드를 직접 설정 (BaseEntity에서 상속받은 id 필드)
    Field idField = BaseEntity.class.getDeclaredField("id");  // BaseEntity에서 id 필드 찾기
    idField.setAccessible(true);
    idField.set(indexInfo, indexInfoId);  // id 필드에 값 설정

    IndexData savedIndexData = new IndexData(
        indexInfo,
        baseDate,
        SourceType.USER,
        new BigDecimal("100.0"),
        new BigDecimal("95.0"),
        new BigDecimal("102.0"),
        new BigDecimal("98.0"),
        new BigDecimal("1.0"),
        new BigDecimal("1.05"),
        500L,
        1000L,
        1000000L
    );

    IndexDataDto indexDataDto = new IndexDataDto(
        1L,
        indexInfo.getId(),
        baseDate,
        SourceType.USER,
        new BigDecimal("100.0"),
        new BigDecimal("95.0"),
        new BigDecimal("102.0"),
        new BigDecimal("98.0"),
        new BigDecimal("1.0"),
        new BigDecimal("1.05"),
        500L,
        1000L,
        1000000L
    );  // 반환할 DTO 객체

    Mockito.when(indexInfoRepository.findById(indexInfoId)).thenReturn(Optional.of(indexInfo));
    Mockito.when(indexDataRepository.existsByIndexInfoIdAndBaseDate(indexInfoId, baseDate)).thenReturn(false);
    Mockito.when(indexDataRepository.save(Mockito.any(IndexData.class))).thenReturn(savedIndexData);
    Mockito.when(indexDataMapper.toDto(savedIndexData)).thenReturn(indexDataDto);

    // When
    IndexDataDto result = indexDataService.create(request);

    // Then
    assertNotNull(result);
    assertEquals(indexDataDto, result);
  }

}

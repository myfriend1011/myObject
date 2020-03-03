package com.chenli.health.serviceinterface;

import java.util.Map;

/**
 * 数据统计接口
 */
public interface ReportService {
    Map<String, Object> getBusinessReport() throws Exception;

}

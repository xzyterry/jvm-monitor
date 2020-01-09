package com.jawnho.service;

import com.jawnho.domain.HostService;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public interface IHostServiceService {

  void insertAll(List<HostService> hostServiceList);

  List<HostService> findAll();
}

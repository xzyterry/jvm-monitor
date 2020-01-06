package com.jawnho.service;

import com.jawnho.domain.HostInfo;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/5
 */
public interface IHostInfoService {

  List<HostInfo> findAll();

}

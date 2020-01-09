package com.jawnho.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jawnho
 * @date 2020/1/8
 */
@CompoundIndexes({
    @CompoundIndex(
        name = "hostIp_1_hostName_1",
        def = "{ 'hostIp' : 1 , 'hostName' : 1}",
        unique = true,
        background = true

    )
})
@Document
@Setter
@Getter
public class HostService {

  private String hostName;

  public static final String HOST_NAME = "hostName";

  private String serviceName;

  public static final String SERVICE_NAME = "serviceName";

}

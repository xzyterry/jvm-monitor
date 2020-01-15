package com.jawnho.domain;

import java.util.Date;
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
        name = "hostName_1_serviceName_1",
        def = "{ 'hostName' : 1 , 'serviceName' : 1}",
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

  private Date createDate;

  public static final String CREATE_DATE = "createDate";

  private Date updateDate;

  public static final String UPDATE_DATE = "updateDate";

}

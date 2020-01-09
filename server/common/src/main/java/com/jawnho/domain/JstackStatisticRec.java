package com.jawnho.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author jawnho
 * @date 2020/1/8
 */
@CompoundIndexes({
    @CompoundIndex(
        name = "minStr_1_dateStr_1_serviceName_1_hostName_1",
        def = "{'minStr' : 1 , 'dateStr' : 1 ,'serviceName' : 1 , 'hostName' : 1}",
        background = true
    )
})
@Setter
@Getter
public class JstackStatisticRec {

  private String dateStr;

  public static final String DATE_STR = "dateStr";

  private String minStr;

  public static final String MIN_STR = "minStr";

  private String hostName;

  public static final String HOST_NAME = "hostName";

  private String serviceName;

  public static final String SERVICE_NAME = "serviceName";

  private String state;

  public static final String STATE = "state";

  private int count;

  public static final String COUNT = "count";

  @Indexed(
      background = true,
      expireAfterSeconds = 2 * 24 * 60 * 60
  )
  private Date createDate;

  public static final String CREATE_DATE = "createDate";

}

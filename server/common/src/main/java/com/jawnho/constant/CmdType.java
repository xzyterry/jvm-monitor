package com.jawnho.constant;

/**
 * @author jawnho
 * @date 2020/1/5
 */
public class CmdType {

  /**
   * JVM信息查看命令
   */
  public static final String J_STAT = "jstat";

  /**
   * 显示gc相关的堆信息,查看gc的次数时间
   *
   * @see GcField
   */
  public static final String GC = "-gc";

  /**
   * 显示三代(young,old,meta) 对象的使用和占用大小
   */
  public static final String GC_CAPACITY = "-gccapacity";

  /**
   * metaspace中对象的信息及占用量
   */
  public static final String GC_META_CAPACITY = "-gcmetacapacity";

  /**
   * young中对象信息
   */
  public static final String GC_NEW = "-gcnew";

  /**
   * young中对象的信息及其占用量
   */
  public static final String GC_NEW_CAPACITY = "-gcnewcapacity";

  /**
   * old对象信息
   */
  public static final String GC_OLD = "-gcold";

  /**
   * old对象的信息及其占用量
   */
  public static final String GC_OLD_CAPACITY = "-gcoldcapacity";

  /**
   * 统计gc信息
   */
  public static final String GC_UTIL = "-gcutil";

  /**
   * 显示垃圾回收的相关信息(同上),同时显示最后一次货当前正在发生的垃圾回收的诱因
   */
  public static final String GC_CAUSE = "-gccause";

}

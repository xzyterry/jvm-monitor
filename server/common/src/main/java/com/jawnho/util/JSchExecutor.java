package com.jawnho.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jawnho.constant.CmdType;
import com.jcraft.jsch.*;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author jawnho
 * @date 2020/1/5
 */
public class JSchExecutor {

  public static void main(String[] args) throws Exception {
    JSchExecutor executor = new JSchExecutor("ws", "xxxd3gfG#", "111.231.93.2");
    executor.connect();
    List<String> retMsgList = executor.execCmd("jstat -gc 20205");
    executor.disconnect();
    int lineNum = 0;
    if (retMsgList.size() != 2) {
      return;
    }
    String retMsg = retMsgList.get(1);
    List<String> result = new LinkedList<>();
    String[] splitArr = retMsg.split(" ");
    for (String split : splitArr) {
      if (Strings.isNullOrEmpty(split) || split.equals(" ")) {
        continue;
      }

      result.add(split);
      System.out.println(split);
    }
  }

  // region init

  private static Logger log = LoggerFactory.getLogger(JSchExecutor.class);

  private String charset = "UTF-8";
  private String user;
  private String passwd;
  private String host;
  private int port = 22;

  private JSch jsch;
  private Session session;

  private ChannelSftp sftp;

  /**
   * @param user 用户名
   * @param passwd 密码
   * @param host 主机IP
   */
  public JSchExecutor(String user, String passwd, String host) {
    this.user = user;
    this.passwd = passwd;
    this.host = host;
  }

  /**
   * @param user 用户名
   * @param passwd 密码
   * @param host 主机IP
   */
  public JSchExecutor(String user, String passwd, String host, int port) {
    this.user = user;
    this.passwd = passwd;
    this.host = host;
    this.port = port;
  }

  // endregion

  /**
   * 连接到指定的IP
   */
  public void connect() throws JSchException {
    jsch = new JSch();
    session = jsch.getSession(user, host, port);
    session.setPassword(passwd);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);
    session.connect();
    Channel channel = session.openChannel("sftp");
    channel.connect();
    sftp = (ChannelSftp) channel;
    log.info("连接到SFTP成功。host: " + host);
  }

  /**
   * 关闭连接
   */
  public void disconnect() {
    if (sftp != null && sftp.isConnected()) {
      sftp.disconnect();
    }
    if (session != null && session.isConnected()) {
      session.disconnect();
    }
  }

  /**
   * 执行一条命令
   */
  public List<String> execCmd(String command) throws Exception {
    log.info("开始执行命令:" + command);
    int returnCode = -1;
    BufferedReader reader = null;
    Channel channel = null;

    channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    channel.setInputStream(null);
    ((ChannelExec) channel).setErrStream(System.err);
    InputStream in = channel.getInputStream();
    reader = new BufferedReader(new InputStreamReader(in));

    channel.connect();
    System.out.println("The remote command is: " + command);
    String buf;
    List<String> result = new LinkedList<>();
    while ((buf = reader.readLine()) != null) {
      result.add(buf);
    }
    reader.close();
    if (channel.isClosed()) {
      returnCode = channel.getExitStatus();
    }
    log.info("Exit-status:" + returnCode);
    channel.disconnect();
    return result;
  }

  /**
   * 执行相关的命令
   */
  public void execCmd() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    String command = "";
    BufferedReader reader = null;
    Channel channel = null;

    try {
      while ((command = br.readLine()) != null) {
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        InputStream in = channel.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in,
            Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null) {
          log.info(buf);
        }
      }
    } catch (Exception e) {
      log.error("exec fail with exception: {}", LogUtil.extractStackTrace(e));
    } finally {
      try {
        reader.close();
      } catch (Exception e) {
        log.error("reader close fail with exception: {}", LogUtil.extractStackTrace(e));
      }
      channel.disconnect();
    }
  }

  /**
   * 上传文件
   */
  public void uploadFile(String local, String remote) throws Exception {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(local));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(remote));

    File file = new File(local);
    if (file.isDirectory()) {
      throw new RuntimeException(local + "  is not a file");
    }

    InputStream inputStream = null;
    try {
      String rpath = remote.substring(0, remote.lastIndexOf("/") + 1);
      if (!isDirExist(rpath)) {
        createDir(rpath);
      }
      inputStream = new FileInputStream(file);
      sftp.setInputStream(inputStream);
      sftp.put(inputStream, remote);
    } catch (Exception e) {
      throw e;
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
  }

  /**
   * 下载文件
   */
  public void downloadFile(String remote, String local) throws Exception {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(local));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(remote));

    OutputStream outputStream = null;
    try {
      sftp.connect(5000);
      outputStream = new FileOutputStream(new File(local));
      sftp.get(remote, outputStream);
      outputStream.flush();
    } catch (Exception e) {
      throw e;
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }
  }

  /**
   * 移动到相应的目录下
   *
   * @param pathName 要移动的目录
   */
  public boolean changeDir(String pathName) {
    if (pathName == null || pathName.trim().equals("")) {
      log.debug("invalid pathName");
      return false;
    }
    try {
      sftp.cd(pathName.replaceAll("\\\\", "/"));
      log.debug("directory successfully changed,current dir=" + sftp.pwd());
      return true;
    } catch (SftpException e) {
      log.error("failed to change directory", e);
      return false;
    }
  }

  /**
   * 创建一个文件目录，mkdir每次只能创建一个文件目录 或者可以使用命令mkdir -p 来创建多个文件目录
   */
  public void createDir(String createPath) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(createPath));

    try {
      if (isDirExist(createPath)) {
        sftp.cd(createPath);
        return;
      }
      String pathArry[] = createPath.split("/");
      StringBuffer filePath = new StringBuffer("/");
      for (String path : pathArry) {
        if (path.equals("")) {
          continue;
        }
        filePath.append(path + "/");
        if (isDirExist(filePath.toString())) {
          sftp.cd(filePath.toString());
        } else {
          // 建立目录
          sftp.mkdir(filePath.toString());
          // 进入并设置为当前目录
          sftp.cd(filePath.toString());
        }
      }
      sftp.cd(createPath);
    } catch (SftpException e) {
      throw new RuntimeException("创建路径错误：" + createPath);
    }
  }


  /**
   * 判断目录是否存在
   */
  public boolean isDirExist(String directory) {
    if (Strings.isNullOrEmpty(directory)) {
      return false;
    }

    boolean isDirExistFlag = false;
    try {
      SftpATTRS sftpATTRS = sftp.lstat(directory);
      isDirExistFlag = true;
      return sftpATTRS.isDir();
    } catch (Exception e) {
      if (e.getMessage().toLowerCase().equals("no such file")) {
        isDirExistFlag = false;
      }
    }
    return isDirExistFlag;
  }

}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.zeppelin.annotation.ZeppelinApi;
import org.apache.zeppelin.interpreter.InterpreterGroup;
import org.apache.zeppelin.interpreter.InterpreterSetting;
import org.apache.zeppelin.interpreter.InterpreterSettingManager;
import org.apache.zeppelin.interpreter.remote.RemoteInterpreterProcess;
import org.apache.zeppelin.server.JsonResponse;
import org.apache.zeppelin.user.AuthenticationInfo;
import org.apache.zeppelin.utils.SecurityUtils;
import org.apache.zeppelin.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interpreter Rest API
 */
@Path("/remoteinterpreter")
@Produces("application/json")
public class RemoteInterpreterRestApi {

  private static final Logger logger = LoggerFactory.getLogger(RemoteInterpreterRestApi.class);
  private InterpreterSettingManager interpreterSettingManager;

  public RemoteInterpreterRestApi() {

  }

  public RemoteInterpreterRestApi(InterpreterSettingManager interpreterSettingManager) {
    this.interpreterSettingManager = interpreterSettingManager;
  }


  /**
   * Restart interpreter setting
   */
  @PUT
  @Path("restart/{replName}")
  @ZeppelinApi
  public Response restartSetting(@PathParam("replName") String replName)
      throws Exception {

    AuthenticationInfo subject = new AuthenticationInfo(SecurityUtils.getPrincipal());
    logger.info("remoteInterpreter restart for user {} interpreter {}",
        subject.getUser(), replName);
    List<InterpreterSetting> interpreterSettingList = interpreterSettingManager.get();
    for (InterpreterSetting interpreter : interpreterSettingList) {
      InterpreterGroup intp = interpreter.getInterpreterGroup(subject.getUser(), "");
      if (interpreter.getName().equals(replName)) {
        intp.shutdown();
      }
    }
    String result = "Restart Success";
    return new JsonResponse<>(Status.OK, "", result).build();
  }

  @GET
  @ZeppelinApi
  public Response getIntp() throws Exception {

    AuthenticationInfo subject = new AuthenticationInfo(SecurityUtils.getPrincipal());
    List<InterpreterSetting> interpreterSettingList = interpreterSettingManager.get();
    List<RemoteInterpreterGroup> remoteInterpreterGroupList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    for (InterpreterSetting interpreter : interpreterSettingList) {
      InterpreterGroup intp = interpreter.getInterpreterGroup(subject.getUser(), "");

      RemoteInterpreterGroup interpreterGroup = new RemoteInterpreterGroup();
      interpreterGroup.setInterpreterGroup(interpreter.getName());
      String status = "STOPED";
      boolean started = false;
      if (null == intp) {
        interpreterGroup.setStartTime("N/A");
        interpreterGroup.setDuration("N/A");
        interpreterGroup.setStatus(status);
        remoteInterpreterGroupList.add(interpreterGroup);
        continue;
      }
      RemoteInterpreterProcess remoteInterpreterProcess = intp
          .getRemoteInterpreterProcess();
      if (null != remoteInterpreterProcess) {
        if (remoteInterpreterProcess.isRunning()) {
          status = "RUNNING";
        }
        Date startTime = remoteInterpreterProcess.getStartTime();
        if (null != startTime) {
          interpreterGroup.setStartTime(sdf.format(startTime));
          interpreterGroup.setDuration(TimeUtil.toReadableDuration(startTime, new Date()));
          started = true;
        }
      }
      if (!started) {
        interpreterGroup.setStartTime("N/A");
        interpreterGroup.setDuration("N/A");
      }
      interpreterGroup.setStatus(status);
      remoteInterpreterGroupList.add(interpreterGroup);
    }
    return new JsonResponse<>(Status.OK, "", remoteInterpreterGroupList).build();
  }

}

class RemoteInterpreterGroup {

  private String interpreterGroup;
  private String status;
  private String startTime;
  private String duration;

  public String getInterpreterGroup() {
    return interpreterGroup;
  }

  public void setInterpreterGroup(String interpreterGroup) {
    this.interpreterGroup = interpreterGroup;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }
}

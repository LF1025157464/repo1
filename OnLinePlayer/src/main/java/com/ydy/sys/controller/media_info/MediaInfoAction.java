package com.ydy.sys.controller.media_info;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.github.pagehelper.Page;
import com.ydy.common.constan.ErrorMsg;
import com.ydy.sys.entity.*;
import com.ydy.sys.service.user_info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ydy.common.exception.QingException;
import com.ydy.common.util.UpLoadVideoUtil;
import com.ydy.sys.service.media_info.MediaInfoService;
import com.ydy.sys.service.type_field.TypeFieldService;
import com.ydy.sys.service.type_info.TypeInfoService;


@Controller
@RequestMapping("media_info")
public class MediaInfoAction {

    @Autowired
    private MediaInfoService mediaInfoService;
    @Autowired
    private TypeInfoService typeInfoService;
    @Autowired
    private TypeFieldService typeFieldService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查询所有媒体信息（正常）
     *
     * @param map
     * @param keyWord  搜索关键字
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list_normal.action")
    public String listNormal(ModelMap map, @RequestParam(required = false, value = "typeId") String typeId,
                             @RequestParam(required = false, value = "keyWord") String keyWord,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 查询分类信息
        List<TypeInfo> typeList = typeInfoService.list();
        map.put("typeList", typeList);

        Map<String, Object> param = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(keyWord)) {
            param.put("keyWord", keyWord.trim());
            map.put("keyWord", keyWord);
        }
        param.put("typeId", typeId);
        map.put("typeId", typeId);

        param.put("status", "1");

        // pageHelper分页插件
        // 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
        PageHelper.startPage(pageNum, pageSize);
        List<MediaInfo> list = mediaInfoService.list(param);
        PageInfo<MediaInfo> pageInfo = new PageInfo<MediaInfo>(list);
        map.put("pageInfo", pageInfo);

        return "admin/media_info/list_normal";
    }

    /**
     * 查询所有媒体信息（正常）
     *
     * @param map
     * @param keyWord  搜索关键字
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list_check.action")
    public String listCheck(ModelMap map, @RequestParam(required = false, value = "typeId") String typeId,
                             @RequestParam(required = false, value = "keyWord") String keyWord,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 查询分类信息
        List<TypeInfo> typeList = typeInfoService.list();
        map.put("typeList", typeList);

        Map<String, Object> param = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(keyWord)) {
            param.put("keyWord", keyWord.trim());
            map.put("keyWord", keyWord);
        }
        param.put("typeId", typeId);
        map.put("typeId", typeId);

        param.put("status", "3");

        // pageHelper分页插件
        // 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
        PageHelper.startPage(pageNum, pageSize);
        List<MediaInfo> list = mediaInfoService.list(param);
        PageInfo<MediaInfo> pageInfo = new PageInfo<MediaInfo>(list);
        map.put("pageInfo", pageInfo);

        return "admin/media_info/list_check";
    }

    /**
     * 查询所有媒体信息（回收站）
     *
     * @param map
     * @param keyWord  搜索关键字
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list_recycle.action")
    public String listRecycle(ModelMap map, @RequestParam(required = false, value = "keyWord") String keyWord,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Map<String, Object> param = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(keyWord)) {
            param.put("keyWord", keyWord.trim());
            map.put("keyWord", keyWord);
        }
        param.put("status", "0");

        // pageHelper分页插件
        // 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
        PageHelper.startPage(pageNum, pageSize);
        List<MediaInfo> list = mediaInfoService.list(param);
        PageInfo<MediaInfo> pageInfo = new PageInfo<MediaInfo>(list);
        map.put("pageInfo", pageInfo);

        return "admin/media_info/list_recycle";
    }

    /**
     * 媒体信息编辑
     *
     * @param map
     * @param typeId  分类主键
     * @param mediaId 媒体主键
     * @return
     */
    @RequestMapping("edit.action")
    public String edit(ModelMap map,
                       @RequestParam(required = false, value = "typeId") String typeId,
                        @RequestParam(required = false, value = "mediaId") String mediaId) {

        System.out.println("fdfd");
        System.out.println(mediaId);
        map.put("typeId", typeId);
        map.put("mediaId", mediaId);

        return "admin/media_info/edit";
    }

    /**
     * 媒体信息保存
     *
     * @return
     * @throws QingException
     * @throws ServletException
     * @throws IOException
     * @throws IllegalStateException
     */
    @RequestMapping("save.json")
    @ResponseBody
    public Result save(HttpServletRequest request,
                       @RequestParam(value = "file", required = false) MultipartFile multipartFile)
            throws QingException, IllegalStateException, IOException, ServletException {

        //获取用户信息
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
        //创建map保存提交信息
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id", userInfo.getId());


        String message = "";
        UpLoadVideoInfo entity = new UpLoadVideoInfo();
        String logoPathDir = "video";
        System.out.println("-------" + logoPathDir + "----------------------------------");
        UpLoadVideoUtil fileUploadTool = new UpLoadVideoUtil();
        try {
            //上传视频文件，得到视频文件上传的信息对象
            //上传本地
            entity = fileUploadTool.createFile(logoPathDir, multipartFile, request);
            //如果信息不为空，表示上传成功
            if (entity != null) {
                mediaInfoService.saveVideo(entity);
                message = "上传成功";
                param.put("entity", entity);
                param.put("url", entity.getFileNamedirs());
                param.put("result", message);
            } else {
                message = "上传失败";
                param.put("result", message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //视频上传结束

        String typeId = request.getParameter("type_id");
        // 分类
        param.put("type_id", typeId);
        // 主键
        param.put("media_id", request.getParameter("media_id"));
        // 海报
        param.put("haibao", request.getParameter("haibao"));
        // 大封面
        param.put("dafengmian", request.getParameter("dafengmian"));
        // 小封面
        param.put("fengmian", request.getParameter("fengmian"));
        //看点
        param.put("kandian", request.getParameter("kandian"));
        //主演
        param.put("zhuyan", request.getParameter("zhuyan"));
        // 标题
        String biaoti = request.getParameter("biaoti");
        if (!StringUtils.isEmpty(biaoti)) {
            biaoti = biaoti.replace("'", "");
        }
        param.put("biaoti", biaoti);
        // 别名
        String bieming = request.getParameter("bieming");
        if (!StringUtils.isEmpty(bieming)) {
            bieming = bieming.replace("'", "");
        }
        param.put("bieming", bieming);
        // 简介
        String jianjie = request.getParameter("jianjie");
        if (!StringUtils.isEmpty(jianjie)) {
            jianjie = jianjie.replace("'", "");
        }
        param.put("jianjie", jianjie);
        System.out.println(param.get("type_id"));

        mediaInfoService.save2(param);
        return Result.success();
    }

    /**
     * 媒体信息保存
     *
     * @return
     * @throws QingException
     * @throws ServletException
     * @throws IOException
     * @throws IllegalStateException
     */
    @RequestMapping("save2.json")
    @ResponseBody
    public Result save2(HttpServletRequest request, @RequestParam(value = "file", required = false)
            MultipartFile multipartFile) throws QingException, IllegalStateException, IOException, ServletException {
        //获取用户信息
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
        //创建map保存提交信息
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id", userInfo.getId());

        String message = "";
        UpLoadVideoInfo entity = new UpLoadVideoInfo();
        String logoPathDir = "video";
        System.out.println("-------" + logoPathDir + "----------------------------------");
        UpLoadVideoUtil fileUploadTool = new UpLoadVideoUtil();

        //获取视频大小
        double fileSize = multipartFile.getSize()/(1024*1024*1024.0);
        System.out.println(fileSize);
        //获取用户剩余空间
        double size = userInfo.getSize();
        if(fileSize > size){
            return Result.error("您视频空间不足，请升级会员！");
        }
        try {
            //上传视频文件，得到视频文件上传的信息对象
            //上传本地
            entity = fileUploadTool.createFile(logoPathDir, multipartFile, request);
            //减去用户空间
            userInfo.setSize(size-fileSize);
            userInfoService.changeSize(userInfo);
            //如果信息不为空，表示上传成功
            if (entity != null) {
                mediaInfoService.saveVideo(entity);
                message = "上传成功";
                param.put("entity", entity);
                param.put("url", entity.getFileNamedirs());
                param.put("size",entity.getSize() );
                param.put("result", message);
            } else {
                message = "上传失败";
                param.put("result", message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //视频上传结束

        String typeId = request.getParameter("type_id");
        // 分类
        param.put("type_id", typeId);
        // 主键
        param.put("media_id", request.getParameter("media_id"));
        // 海报
        param.put("haibao", request.getParameter("haibao"));
        // 大封面
        param.put("dafengmian", request.getParameter("dafengmian"));
        // 小封面
        param.put("fengmian", request.getParameter("fengmian"));
        //看点
        param.put("kandian", request.getParameter("kandian"));
        //主演
        param.put("zhuyan", request.getParameter("zhuyan"));
        // 标题
        String biaoti = request.getParameter("biaoti");
        if (!StringUtils.isEmpty(biaoti)) {
            biaoti = biaoti.replace("'", "");
        }
        param.put("biaoti", biaoti);
        // 别名
        String bieming = request.getParameter("bieming");
        if (!StringUtils.isEmpty(bieming)) {
            bieming = bieming.replace("'", "");
        }
        param.put("bieming", bieming);
        // 简介
        String jianjie = request.getParameter("jianjie");
        if (!StringUtils.isEmpty(jianjie)) {
            jianjie = jianjie.replace("'", "");
        }
        param.put("jianjie", jianjie);
        System.out.println(param.get("type_id"));

        mediaInfoService.save(param);
        return Result.success();
    }

    /**
     * 根据主键，获取媒体信息
     * @param mediaId 媒体信息的主键
     * @return
     */
    @RequestMapping("get_media_info.json")
    @ResponseBody
    public Result getMediaInfo(
            @RequestParam(value="mediaId") String mediaId,
            @RequestParam(value="typeId") String typeId) {

        Map<String, Object> mediaInfo = mediaInfoService.selectByIdAndTypeId(mediaId, typeId);

        return Result.success().add("mediaInfo", mediaInfo);
    }

    /**
     * 批量更新媒体的状态
     *
     * @param mediaIdArr 主键数组
     * @param status     状态
     * @return
     */
    @RequestMapping("batch_update_status.json")
    @ResponseBody
    public Result batchUpdateStatus(@RequestParam(value = "mediaIdArr") String[] mediaIdArr,
                                    @RequestParam(value = "status") String status) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mediaIdArr", mediaIdArr);
        param.put("status", status);

        mediaInfoService.batchUpdateStatus(param);

        return Result.success();
    }

    //用户改变自己的视频状态
    @RequestMapping("update_status.json")
    @ResponseBody
    public Result UpdateStatus(@RequestParam(value = "mediaId") String mediaId,
                                    @RequestParam(value = "status") String status) {
        //0，封禁 1，公开 2，私有 3，公开待审核
        if("0".equals(status)){
            //被封禁
        }else if("1".equals(status)){
            //公开转私有
            status = "2";
        }else if("2".equals(status)){
            //私有转公开待审核
            status = "3";
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mediaIdArr", new String[]{mediaId});
        param.put("status", status);

        mediaInfoService.UpdateStatus(param);
        return Result.success();
    }
    /**
     * 批量移动到分类
     *
     * @param mediaIdArr 主键数组
     * @param typeId     分类id
     * @return
     */
    @RequestMapping("batch_change_type.json")
    @ResponseBody
    public Result batchUpdateType(@RequestParam(value = "mediaIdArr") String[] mediaIdArr,
                                  @RequestParam(value = "typeId") String typeId) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mediaIdArr", mediaIdArr);
        param.put("typeId", typeId);

        mediaInfoService.batchUpdateType(param);

        return Result.success();
    }

    /**
     * 批量删除
     *
     * @param mediaIdArr 主键数组
     * @return
     */
    @RequestMapping("batch_delete.json")
    @ResponseBody
    public Result batchDelete(@RequestParam(value = "mediaIdArr") String[] mediaIdArr) {

        mediaInfoService.batchDelete(mediaIdArr);

        return Result.success();
    }



    /**
     * 获取我的视频
     * @param request
     * @param userToken 用户凭证
     * @param pageNum
     * @param pageSize
     * @return
     * @throws QingException
     */
    @RequestMapping("get_media_list.json")
    @ResponseBody
    public Result getMediaList(
            HttpServletRequest request,
            @RequestParam(value="userToken") String userToken,
            @RequestParam(value="pageNum", defaultValue="1") int pageNum,
            @RequestParam(value="pageSize", defaultValue="5") int pageSize) throws QingException {

        if (StringUtils.isEmpty(userToken)) {
            throw new QingException(ErrorMsg.ERROR_100012);
        }

        // 判断session
        HttpSession session  = request.getSession();
        // 从session中取出用户身份信息
        UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

        if (userInfo==null) {
            userInfo = userInfoService.getUserInfoByUserToken(userToken);
            // 将用户信息保存进session
            request.getSession().setAttribute("userInfo", userInfo);
        }

        // pageHelper分页插件
        // 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        //c查询用户的所有视频
        List<Map<String, Object>> list = mediaInfoService.listMedia(userInfo.getId());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return Result.success().add("pageInfo", pageInfo);
    }

    /**
     * 删除用户自己上传的视频
     * @param userToken
     * @param request
     * @param mediaId
     * @return
     * @throws QingException
     */
    @RequestMapping("delete_my_media.json")
    @ResponseBody
    public Result deleteMyMedia(
            @RequestParam(value="userToken") String userToken,
            HttpServletRequest request,
            @RequestParam(value="mediaId") String mediaId) throws QingException {

        if (StringUtils.isEmpty(userToken)) {
            throw new QingException(ErrorMsg.ERROR_100012);
        }

        // 判断session
        HttpSession session  = request.getSession();
        // 从session中取出用户身份信息
        UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

        if (userInfo==null) {
            userInfo = userInfoService.getUserInfoByUserToken(userToken);
            // 将用户信息保存进session
            request.getSession().setAttribute("userInfo", userInfo);
        }

        //删除该用户下的给视频
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMediaId(mediaId);
        mediaInfo.setUserId(userInfo.getId());

        mediaInfoService.deleteMyMedia(mediaInfo);
        return Result.success();
    }



}

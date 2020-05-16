package com.ydy.sys.service.media_info;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ydy.sys.entity.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.sys.dao.media_info.IMediaInfoDAO;
import com.ydy.sys.dao.video_info.IVideoInfoDAO;
import com.ydy.sys.entity.MediaInfo;
import com.ydy.sys.entity.UpLoadVideoInfo;

@Service("MediaInfoService")
public class MediaInfoService {
    @Autowired
    private IMediaInfoDAO iMediaInfoDAO;
    @Autowired
    private IVideoInfoDAO iVideoInfoDAO;

    /**
     * 查询媒体列表
     *
     * @param param
     * @return
     */
    public List<MediaInfo> list(Map<String, Object> param) {
        return iMediaInfoDAO.list(param);
    }

    /**
     * 保存媒体信息
     *
     * @param param
     * @throws QingException
     */
    public void save(Map<String, Object> param) throws QingException {
        //视频的url
        String url = param.get("url").toString();
        //**************************************
        System.out.println(url);
        //Play_id
        String play_id = "1";
        // 分类
        String typeId = param.get("type_id").toString();
        // 标题
        String biaoti = param.get("biaoti").toString();
        // 新增
        String media_id = UUID.randomUUID().toString();


        String bieming = param.get("bieming").toString();

        String dafengmian = param.get("dafengmian").toString();

        String fengmian = param.get("fengmian").toString();

        String haibao = param.get("haibao").toString();

        String jianjie = param.get("jianjie").toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMediaId(media_id);
        mediaInfo.setTypeId(Integer.parseInt(typeId));
        mediaInfo.setUpdateTime(new Date());
        mediaInfo.setBiaoti(biaoti);
        mediaInfo.setBieming(bieming);
        mediaInfo.setDafengmian(dafengmian);
        mediaInfo.setFengmian(fengmian);
        mediaInfo.setHaibao(haibao);
        mediaInfo.setJianjie(jianjie);
        //视频状态设置为私有
        mediaInfo.setStatus("2");
        mediaInfo.setUrl(url);
        mediaInfo.setPlayId(1);
        //设置视频所有者
        mediaInfo.setUserId((String)param.get("user_id"));
        mediaInfo.setSize((String)param.get("size"));
        iMediaInfoDAO.insert(mediaInfo);



        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setMediaId(media_id);
        videoInfo.setImage(haibao);
        videoInfo.setTitle(biaoti);
        videoInfo.setUrl(url);
        videoInfo.setPlayerId(play_id);
        //视频状态设置为私有
        videoInfo.setStatus("2");
        videoInfo.setUpdateTime(updateTime.toString());
        iVideoInfoDAO.insertVideo(videoInfo);
    }


    /**
     * 保存媒体信息,默认为公开status=1
     *
     * @param param
     * @throws QingException
     */
    public void save2(Map<String, Object> param) throws QingException {
        //视频的url
        String url = param.get("url").toString();
        //**************************************
        System.out.println(url);
        //Play_id
        String play_id = "1";
        // 分类
        String typeId = param.get("type_id").toString();
        // 标题
        String biaoti = param.get("biaoti").toString();
        // 新增
        String media_id = UUID.randomUUID().toString();


        String bieming = param.get("bieming").toString();

        String dafengmian = param.get("dafengmian").toString();

        String fengmian = param.get("fengmian").toString();

        String haibao = param.get("haibao").toString();

        String jianjie = param.get("jianjie").toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMediaId(media_id);
        mediaInfo.setTypeId(Integer.parseInt(typeId));
        mediaInfo.setUpdateTime(new Date());
        mediaInfo.setBiaoti(biaoti);
        mediaInfo.setBieming(bieming);
        mediaInfo.setDafengmian(dafengmian);
        mediaInfo.setFengmian(fengmian);
        mediaInfo.setHaibao(haibao);
        mediaInfo.setJianjie(jianjie);
        //视频状态设置为私有
        mediaInfo.setStatus("1");
        mediaInfo.setUrl(url);
        mediaInfo.setPlayId(1);
        //设置视频所有者
        mediaInfo.setUserId((String)param.get("user_id"));
        iMediaInfoDAO.insert(mediaInfo);


        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setMediaId(media_id);
        videoInfo.setImage(haibao);
        videoInfo.setTitle(biaoti);
        videoInfo.setUrl(url);
        videoInfo.setPlayerId(play_id);
        //视频状态设置为私有
        videoInfo.setStatus("1");
        videoInfo.setUpdateTime(updateTime.toString());
        iVideoInfoDAO.insertVideo(videoInfo);
    }

    /**
     * 根据主键查询媒体信息
     *
     * @param mediaId 主键
     * @return
     */
    public MediaInfo selectById(String mediaId) {
        return iMediaInfoDAO.selectById(mediaId);
    }

    /**
     * 根据主键，查询该媒体信息标题
     *
     * @param mediaId 主键
     * @return
     */
    public String selectBiaotiById(String mediaId) {
        return iMediaInfoDAO.selectBiaotiById(mediaId);
    }


    /**
     * 根据主键和分类id，获取媒体字段信息
     * @param mediaId 主键
     * @param typeId 分类id
     * @return
     */
    public Map<String, Object> selectByIdAndTypeId(String mediaId, String typeId) {
        // 1.0 根据分类id，查询该媒体使用了哪些字段
        /*List<FieldInfo> list = iFieldInfoDAO.listByTypeId(typeId);
        if (list!=null && list.isEmpty()==false) {
            // 生成媒体查询sql（查哪些字段）
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT ");
            for (int i=0; i<list.size(); i++) {
                if (i==(list.size()-1)) {
                    sql.append(list.get(i).getVarName());
                } else {
                    sql.append(list.get(i).getVarName() + ",");
                }
            }
            sql.append(" FROM ");
            sql.append(" media_info ");
            sql.append(" WHERE ");
            sql.append(" media_id = '" + mediaId + "' ");

            // 返回查询结果
            return iMediaInfoDAO.selectSqlById(sql.toString());
        }*/

        //如果mediaId不为空
        if(mediaId != null && !StringUtils.isEmpty(mediaId)){
            String sql = "select * from media_info where media_id = '" + mediaId + "'";
            return iMediaInfoDAO.selectSqlById(sql);
        }

        return null;
    }
    /**
     * 根据主键，获取媒体信息
     *
     * @param mediaId 媒体信息的主键
     * @return
     * @throws QingException
     */
    public Map<String, Object> selectByMediaId(String mediaId) throws QingException {
        Map<String, Object> map = iMediaInfoDAO.selectByMediaId(mediaId);
        if (map == null) {
            throw new QingException(ErrorMsg.ERROR_300003);
        }

        // 遍历map，获取字段对应的文本
        for (String key : map.keySet()) {
            if ("media_id".equals(key) || "tag".equals(key) || "haibao".equals(key) || "biaoti".equals(key) || "kandian".equals(key) || "jianjie".equals(key)) {

            } else {
                String fieldProfileId = map.get(key).toString();
                String[] arr = fieldProfileId.split(",");
            }
        }
        return map;
    }

    /**
     * 搜索
     *
     * @param keyWord 搜索关键词
     * @return
     * @throws QingException
     */
    public List<String> search(String keyWord) throws QingException {
        if (StringUtils.isEmpty(keyWord)) {
            return null;
        }

        return iMediaInfoDAO.searchIdByKeyWord(keyWord);
    }

    /**
     * 批量更新媒体的状态
     *
     * @param param
     */
    public void batchUpdateStatus(Map<String, Object> param) {
        // 批量更新媒体的状态
        iMediaInfoDAO.batchUpdate(param);

        // 批量更新视频状态
        iVideoInfoDAO.batchUpdate(param);
    }

    /**
     * 批量移动到分类
     *
     * @param param
     */
    public void batchUpdateType(Map<String, Object> param) {
        iMediaInfoDAO.batchUpdate(param);
    }

    /**
     * 批量删除
     *
     * @param mediaIdArr 主键数组
     */
    public void batchDelete(String[] mediaIdArr) {
        // 1.0 删除其下的视频信息
        iVideoInfoDAO.batchDeleteByMediaId(mediaIdArr);

        // 2.0 删除媒体信息
        iMediaInfoDAO.batchDelete(mediaIdArr);
    }

    /**
     * 视频上传
     */
    public void saveVideo(UpLoadVideoInfo upLoadVideoInfo) {
        System.out.println(upLoadVideoInfo.getFileNamedirs());
        iMediaInfoDAO.saveVideo(upLoadVideoInfo);
    }

    public List<MediaInfo> selectListMediaByUser(Map<String, Object> param) {
        return iMediaInfoDAO.selectListMediaByUser(param);
    }

    public List<Map<String,Object>> listMedia(String id) {
        return iMediaInfoDAO.listMedia(id);
    }

    public void deleteMyMedia(MediaInfo mediaInfo) {
        iMediaInfoDAO.deleteMediaByUserAndMediaId(mediaInfo);
        iVideoInfoDAO.batchDeleteByMediaId(new String[]{mediaInfo.getMediaId()});
    }

    //更新用户视频状态
    public void UpdateStatus(Map<String, Object> param) {
        // 批量更新媒体的状态
        iMediaInfoDAO.Update(param);

        // 批量更新视频状态
        iVideoInfoDAO.batchUpdate(param);
    }

    //获取用户下的公开视频
    public List<Map<String,Object>> selectMediaByUserId(String userId) {
        return iMediaInfoDAO.selectMediaByUserId(userId);
    }
}

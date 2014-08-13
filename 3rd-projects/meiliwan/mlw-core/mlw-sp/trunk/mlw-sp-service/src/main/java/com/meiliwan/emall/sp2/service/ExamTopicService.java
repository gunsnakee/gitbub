package com.meiliwan.emall.sp2.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.ExamLog;
import com.meiliwan.emall.sp2.bean.ExamResult;
import com.meiliwan.emall.sp2.bean.ExamTopic;
import com.meiliwan.emall.sp2.dao.ExamLogDao;
import com.meiliwan.emall.sp2.dao.ExamResultDao;
import com.meiliwan.emall.sp2.dao.ExamTopicDao;
import com.meiliwan.emall.sp2.dto.ExamAnswerVo;
import com.meiliwan.emall.sp2.dto.ExamParams;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 答题系统业务层
 * User: wuzixin
 * Date: 14-4-24
 * Time: 下午6:31
 */
@Service
public class ExamTopicService extends DefaultBaseServiceImpl {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    @Autowired
    private ExamTopicDao topicDao;
    @Autowired
    private ExamResultDao resultDao;
    @Autowired
    private ExamLogDao logDao;

    private final static String exportPath = "/data/exam/exportfiles/";

    /**
     * 随机获取一道题目
     */
    public void getTopicByRand(JsonObject resultObj) {
        ExamTopic topic = topicDao.getTopicByRand();
        if (topic != null && StringUtils.isNotEmpty(topic.getAnswerJson())) {
            List<ExamAnswerVo> answerVos = new Gson().fromJson(topic.getAnswerJson(), new TypeToken<List<ExamAnswerVo>>() {
            }.getType());
            topic.setAnswers(answerVos);
        }
        addToResult(topic, resultObj);
    }

    /**
     * 根据用户ID和当天时间 查询用户当天答题的机会次数
     *
     * @param uid
     */
    public void getCountAnswerGroupNum(JsonObject resultObj, int uid) {
        int count = logDao.getCountAnswerGroupNum(uid);
        addToResult(count, resultObj);
    }

    /**
     * 获取用户答题得到的礼包等级数
     *
     * @param resultObj
     * @param uid
     */
    public void getExamResultByUid(JsonObject resultObj, int uid) {
        ExamResult result = resultDao.getEntityById(uid);
        int level = 0;
        if (result != null) {
            level = result.getRewardLevel();
        }
        addToResult(level, resultObj);
    }

    /**
     * 增加每组答题的操作记录，记录每组答题的数目,开始答题
     *
     * @param resultObj
     * @param uid
     */
    public void addExamLog(JsonObject resultObj, int uid) {
        ExamLog log = new ExamLog();
        int id = 0;
        log.setUid(uid);
        log.setAnswerDate(DateUtil.getDateStr(new Date()));
        log.setTopicCount(0);
        logDao.insert(log);
        id = log.getId();
        addToResult(id, resultObj);
    }

    /**
     * 修改对应题目组的状态
     *
     * @param groupId
     * @param uid
     * @return
     */
    public void updateGroupState(JsonObject resultObj, int groupId, int uid) {
        int count = logDao.updateGroupState(groupId, uid);
        addToResult(count, resultObj);
    }

    /**
     * 判断答题是否正确，同时判断是否礼包等级是否增加
     * error表示无法进行下一组答题，next 表示可以进行继续答题，ok表示已经答完三道题目
     * 如果返回是ok，那么直接判断是否该用户的礼包已经大于3，大于三则不曾经，否则增加
     *
     * @param resultObj
     * @param params    参数列表
     * @return error表示无法进行下一组答题，next 表示可以进行继续答题，ok表示已经答完三道题目
     */
    public void checkAnswerResult(JsonObject resultObj, ExamParams params) {
        ExamTopic topic = topicDao.getEntityById(params.getTopicId());
        String suc = "error";
        if (topic != null) {
            if (topic.getCorrectVal().equals(params.getAnwser())) {
                //先获取改组的数据
                ExamLog log = new ExamLog();
                log.setId(params.getGroupId());
                log.setUid(params.getUid());
                log.setAnswerDate(DateUtil.getDateStr(new Date()));
                ExamLog lg = logDao.getEntityByObj(log);
                if (lg != null) {
                    if (lg.getState() != 0) {
                        if (lg.getTopicCount() < 3) {
                            //对应组的答题数增加1
                            int count = logDao.updateCountAddOne(params.getGroupId());
                            if (count > 0) {
                                int tpNum = lg.getTopicCount() + 1;
                                if (tpNum < 3) {
                                    suc = "next"; //表示还可以进行下一步答题
                                } else if (tpNum == 3) {
                                    addExamResultByUid(params.getUid(), params.getUserName());//增加对应用户礼包等级数
                                    suc = "ok"; //表示改组已经达了三题，礼包等级加1
                                }
                            }
                        }
                    }
                }
            }
        }
        addToResult(suc, resultObj);
    }

    public void exportUserExamLevel(JsonObject resultObj) {
        List<ExamResult> list = resultDao.getAllEntityObj();
        boolean suc = false;
        if (list != null && list.size() > 0) {
            String sheet1Name = "用户获取礼包等级";
            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet = wb.createSheet(sheet1Name);
            String[] titles = {"用户ID", "用户昵称", "获取礼品等级"};
            HSSFRow row = sheet.createRow((short) 0);
            // 填充标题
            int i = 0;
            for (String s : titles) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(s);
                i++;
            }
            int rowNum = 1;//新行号
            int size = list.size();
            for (int j = 0; j < size; j++) {
                ExamResult result = list.get(j);
                HSSFRow rowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue(result.getUid());
                rowdata.createCell(1).setCellValue(result.getUserName());
                rowdata.createCell(2).setCellValue(result.getRewardLevel());
                rowNum++;
            }
            String writeName = "exam-"+DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".xls";
            File file = new File(exportPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String path = exportPath + writeName;
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(path);
                wb.write(fileOut);
                fileOut.close();
                suc = true;
            } catch (FileNotFoundException e) {
                logger.error(e, "导出用户获取礼包等级数据失败", "");
            } catch (IOException e) {
                logger.error(e, "导出用户获取礼包等级数据失败", "");
            }
        }
        if (suc) {
            resultDao.updateLevelToZero();
        }
        addToResult(suc, resultObj);
    }

    /**
     * 增加对应用户的礼品等级数
     * 先判断是否存在用户，存在则礼包等级数增加1，否则增加一条记录
     *
     * @param uid
     * @param userName
     */
    private int addExamResultByUid(int uid, String userName) {
        ExamResult result = resultDao.getEntityById(uid);
        int count = 0;
        if (result != null) {
            //判断一周内用户的礼包等级数是否已经达到最大等级，达到则等级不增加，否则增加1
            if (result.getRewardLevel() < 3) {
                count = resultDao.updateLevelToAddOne(uid);
            } else {
                count = 1;
            }
        } else {
            ExamResult rs = new ExamResult();
            rs.setUid(uid);
            rs.setUserName(userName);
            rs.setRewardLevel(1);
            rs.setUpdateTime(new Date());
            rs.setCreateTime(new Date());
            count = resultDao.insert(rs);
        }
        return count;
    }

    public int addTopic(ExamTopic topic) {
        int count = topicDao.insert(topic);
        return count;
    }

}

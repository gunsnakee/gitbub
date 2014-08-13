package com.meiliwan.emall.bkstage.web.controller.pms;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.CategoryHelper;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.client.ProCategoryClient;

/**
 * User: wuzixin
 * Date: 13-6-4
 * Time: 下午7:29
 * 商品类目管理实现
 */
@Controller("pmsCategoryController")
@RequestMapping("/pms/category")
public class CategoryController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 商品类目管理
     */
    @RequestMapping("/list")
    public String list(Locale locale, Model model, HttpServletRequest request) {
        //获取类目的父级ID
        int parentId = ServletRequestUtils.getIntParameter(request, "pid", 0);
        //获取类目级别，用于判断添加商品的类目
        int level = ServletRequestUtils.getIntParameter(request, "level", 1);
        //用于判断是否要查找树形结构
        if (parentId == 0) {
            List<ProCategory> list = ProCategoryClient.getAllCategory();
            String stringHtml = CategoryHelper.categoryTree(list, 0, "/pms/category/list", 1, "add-category");
            model.addAttribute("stringHtml", stringHtml);
        }
        ProCategory proCategory = new ProCategory();
        proCategory.setParentId(parentId);
        List<ProCategory> pcList = ProCategoryClient.getListByPId(proCategory);
        model.addAttribute("pcList", pcList);
        model.addAttribute("pid", parentId);
        model.addAttribute("level", level);
        return "/pms/category/list";
    }

    /**
     * 增加商品类目
     */
    @RequestMapping("/add")
    public String add(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //获取类目的父级ID
        int pid = ServletRequestUtils.getIntParameter(request, "pid", 0);
        try {
            if (isHandle > 0) {
                //获取类目名称
                String categoryName = ServletRequestUtils.getStringParameter(request, "categoryName");
                //获取描述
                String keyword = ServletRequestUtils.getStringParameter(request, "keyword");
                //获取排序类型
                int sequence = ServletRequestUtils.getIntParameter(request, "sequence", 0);
                //获取登陆用户ID
                int adminId = StageHelper.getLoginUser(request).getBksAdmin().getAdminId();
                //用于跳转到制定URL
                int level = ServletRequestUtils.getIntParameter(request, "level", 0);

                ProCategory proCategory = new ProCategory();
                proCategory.setParentId(pid);
                proCategory.setCategoryName(categoryName);
                proCategory.setAdminId(adminId);
                proCategory.setSeachKeyword(keyword);
                proCategory.setSequence(sequence);
                proCategory.setState((short) 1);
                try {
                    boolean success = ProCategoryClient.addProCategory(proCategory);
                    if (success) {
                        return StageHelper.writeDwzSignal("200", "添加成功", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "添加失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
                    }
                } catch (Exception e) {
                    logger.error(e, proCategory, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "添加失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
                }

            } else {
                int level = ServletRequestUtils.getIntParameter(request, "level", 0);
                model.addAttribute("pid", pid);
                model.addAttribute("level", level);
                return "/pms/category/add";
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "isHandle:" + isHandle + ",pid:" + pid, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
        } catch (Exception e) {
            logger.error(e, "isHandle:" + isHandle + ",pid:" + pid, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
        }
    }

    /**
     * 修改商品类目
     */
    @RequestMapping("/update")
    public String update(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //获取类目ID
        int cgtId = ServletRequestUtils.getIntParameter(request, "categoryId", 0);
        try {
            if (isHandle > 0) {
                //获取类目名称
                String categoryName = ServletRequestUtils.getStringParameter(request, "categoryName", "");
                //获取排序类型
                int sequence = ServletRequestUtils.getIntParameter(request, "sequence", 0);
                //获取描述
                String keyword = ServletRequestUtils.getStringParameter(request, "keyword", "");
                //用于跳转到制定URL
                int pid = ServletRequestUtils.getIntParameter(request, "pid", 0);
                int level = ServletRequestUtils.getIntParameter(request, "level", 0);
                //获tr取登陆用户ID
                int adminId = StageHelper.getLoginUser(request).getBksAdmin().getAdminId();
                ProCategory proCategory = new ProCategory();
                proCategory.setCategoryId(cgtId);
                proCategory.setCategoryName(categoryName);
                proCategory.setAdminId(adminId);
                proCategory.setSeachKeyword(keyword);
                proCategory.setSequence(sequence);
                try {
                    boolean success = ProCategoryClient.updateProCategory(proCategory);
                    if (success) {
                        return StageHelper.writeDwzSignal("200", "修改成功", "43", StageHelper.DWZ_CLOSE_CURRENT, "/pms/category/list", response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "修改失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
                    }
                } catch (Exception e) {
                    logger.error(e, proCategory, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "修改失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
                }

            } else {
                int level = ServletRequestUtils.getIntParameter(request, "level", 0);
                ProCategory proCategory = ProCategoryClient.getProCategoryById(cgtId);
                model.addAttribute("pc", proCategory);
                model.addAttribute("level", level);
                return "/pms/category/update";
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "isHandle:" + isHandle + ",cgtId:" + cgtId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "修改失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
        } catch (Exception e) {
            logger.error(e, "isHandle:" + isHandle + ",cgtId:" + cgtId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "修改失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
        }

    }

    /**
     * 删除商品类目
     */
    @RequestMapping("/delete")
    public String delete(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
        //获取删除类目的等级
        int level = ServletRequestUtils.getIntParameter(request, "level", 1);
        //获取类目父级ID
        int pid = ServletRequestUtils.getIntParameter(request, "pid", 0);
        //获取类目ID
        int categoryId = ServletRequestUtils.getIntParameter(request, "categoryId", 0);
        try {
            boolean success = ProCategoryClient.deleteProCategory(categoryId, level);
            if (success) {
                return StageHelper.writeDwzSignal("200", "删除成功", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
            } else {
                return StageHelper.writeDwzSignal("300", "存在该类目的使用，无法删除", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "level:" + level + ",pid:" + pid + ",categoryId:" + categoryId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "删除失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
        } catch (Exception e) {
            logger.error(e, "level:" + level + ",pid:" + pid + ",categoryId:" + categoryId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "删除失败", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);

        }

    }

    /**
     * 生成全部类目页Html
     */
    @RequestMapping(value = "/create-all-category")
    public void createAllCategory(HttpServletRequest request, HttpServletResponse response){
        String servers = null;
        try {
            servers = ConfigOnZk.getInstance().getValue("web/system.properties", "web");
        } catch (BaseException e) {
            logger.error(new RuntimeException("生成全部类目-获取配置文件失败",e), "", WebUtils.getIpAddr(request));
        }
        if (servers != null) {
            String[] servers_list = servers.split(",");
            try {
                for (String server : servers_list) {
                    HttpClientUtil.getInstance().doGet(server + "/category/list");
                }
                StageHelper.writeDwzSignal("200", "成功", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
            } catch (Exception e) {
                e.printStackTrace();
                StageHelper.writeDwzSignal("300", "操作失败,找不到路径！", "43", StageHelper.DWZ_FORWARD, "/pms/category/list", response);
            }
        }
    }
}

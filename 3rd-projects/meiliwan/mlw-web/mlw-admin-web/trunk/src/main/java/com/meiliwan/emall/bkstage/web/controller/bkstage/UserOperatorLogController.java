package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.bean.BksUserOperateLog;
import com.meiliwan.emall.bkstage.client.BksUserOperateLogClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sean on 13-10-18.
 */
@RequestMapping("/bkstage/userOperatorLog")
@Controller("bkstageUserOperatorLogController")
public class UserOperatorLogController {

    @InitBinder
    public void dataBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        PropertyEditor propertyEditor = new CustomDateEditor(dateFormat, true ); // 第二个参数表示是否允许为空
        binder.registerCustomEditor(Date.class , propertyEditor);
    }

    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute BksUserOperateLog query) {
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc",100);
        PagerControl<BksUserOperateLog> pc = BksUserOperateLogClient.getBksUserOperateLogPaperByObj(query, pageInfo);
        model.addAttribute("pc", pc);
        model.addAttribute("obj",query);
        return "/bkstage/userOperatorLog/list";
    }
}

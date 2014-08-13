package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.bkstage.web.html.IndexTreeTemplate;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Sean on 13-5-29.
 */
@Controller("bkstageIndexController")
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String home(Locale locale, Model model, HttpServletRequest request) throws InterruptedException, KeeperException {
        LoginUser loginUser = StageHelper.getLoginUser(request);
        if (loginUser != null) {
            String menuStr = IndexTreeTemplate.getMenuString(loginUser.getMenus().values(), 0);
            model.addAttribute("menuHtml", menuStr);
            return "/index";
        }
        return "redirect:/";
    }
}

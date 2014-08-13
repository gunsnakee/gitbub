package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.web.StageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Sean on 13-11-4.
 */
@Controller("stageHelperController")
@RequestMapping("/bkstage/stage")
public class StageHelperController {


    @RequestMapping("/selectSaveState")
    public void selectSaveState(HttpServletRequest request, HttpServletResponse response) {

        String ctrId = ServletRequestUtils.getStringParameter(request, "ctrId", null);
        int optType = ServletRequestUtils.getIntParameter(request, "optType", 0);
        Map<String, String> returnObj = StageHelper.optSelectSaveState(request, ctrId, null, optType);
        StageHelper.writeJson(response, returnObj);
    }
}

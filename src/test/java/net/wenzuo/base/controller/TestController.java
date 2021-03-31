package net.wenzuo.base.controller;

import net.wenzuo.base.util.Ret;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanghao13
 * @since 2021/3/31 09:58
 */
@RestController
public class TestController {

    @RequestMapping("/err")
    public Ret<Void> error() {
        System.out.println("error");
        int i = 1 / 0;
        return Ret.ok();
    }

}

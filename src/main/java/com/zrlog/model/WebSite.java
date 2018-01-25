package com.zrlog.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存放全局的设置，比如网站标题，关键字，插件，主题的配置信息等，当字典表处理即可，对应数据库的website表
 */
public class WebSite extends Model<WebSite> {
    public static final WebSite dao = new WebSite();

    public Map<String, Object> getWebSite() {
        Map<String, Object> webSites = new HashMap<>();
        List<WebSite> lw = find("select * from website");
        for (WebSite webSite : lw) {
            webSites.put(webSite.getStr("name"), webSite.get("value"));
            webSites.put(webSite.getStr("name") + "Remark", webSite.get("remark"));
        }
        return webSites;
    }

    public boolean updateByKV(String name, Object value) {
        if (Db.queryInt("select siteId from website where name=?", name) != null) {
            Db.update("update website set value=? where name=?", value, name);
        } else {
            Db.update("insert website(`value`,`name`) value(?,?)", value, name);
        }
        return true;
    }

    public String getStringValueByName(String name) {
        WebSite webSite = findFirst("select value from website where name=?", name);
        if (webSite != null) {
            return webSite.get("value");
        }
        return "";
    }

    public boolean getBoolValueByName(String name) {
        WebSite webSite = findFirst("select value from website where name=?", name);
        if (webSite != null) {
            //数据库varchar导致这里使用1进行比较
            if (webSite.get("value") instanceof String) {
                return "1".equals(webSite.get("value"));
            }
        }
        return false;
    }
}

package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.entity.Param;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ParamService {
    @Inject
    DBService dbService;

    public Param convert(Param param)   {
        param.setStage1(getNameUsers(param.getStage1()));
        param.setStage21(getNameUsers(param.getStage21()));
        param.setStage22(getNameUsers(param.getStage22()));
        param.setStage23(getNameUsers(param.getStage23()));
        param.setStage3(getNameUsers(param.getStage3()));
        param.setAuthorize(getNameUsers(param.getAuthorize()));
        if (param.getDelivery()!=null && !param.getDelivery().equals("0") && !param.getDelivery().equals("-1")) {
            param.setDelivery(dbService.getUserById(Integer.parseInt(param.getDelivery())).split("=")[0]);
        }
        return param;
    }

    private List<String> getNameUsers(List<String> idList) {
        List<String> result = new ArrayList<>();
        if (idList.size() == 1 && idList.get(0).equals("-1"))   {
            result.add("-1");
        }   else {
            for (String id : idList) {
                String nameUser = dbService.getUserById(Integer.parseInt(id)).split("=")[0];
                result.add(nameUser);
            }
        }
        return result;
    }
}

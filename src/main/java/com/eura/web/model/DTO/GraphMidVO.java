package com.eura.web.model.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphMidVO {
    List<Integer> goodList;
    List<Integer> badList;
    List<Integer> TotcalCntList;
    List<String> lvlList;
}

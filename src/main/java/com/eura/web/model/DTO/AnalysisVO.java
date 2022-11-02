package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AnalysisVO extends BaseVO{
    double joy;
    double sadness;
    double disgust;
    double contempt;
    double anger;
    double fear;
    double surprise;
    double valence;
    double engagement;
    Date Timestamp;
    String FileName;
    double smile;
    double innerBrowRaise;
    double browRaise;
    double browFurrow;
    double noseWrinkle;
    double upperLipRaise;
    double lipCornerDepressor;
    double chinRaise;
    double lipPucker;
    double lipPress;
    double lipSuck;
    double mouthOpen;
    double smirk;
    double eyeClosure;
    double attention;
    double lidTighten;
    double jawDrop;
    double dimpler;
    double eyeWiden;
    double cheekRaise;
    double lipStretch;
}
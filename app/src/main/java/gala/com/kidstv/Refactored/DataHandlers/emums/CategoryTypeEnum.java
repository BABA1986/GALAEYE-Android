package gala.com.kidstv.Refactored.DataHandlers.emums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CategoryTypeEnum {

    public static final int ECategoryTypePager = 0;
    public static final int ECategoryTypeMedia = 1;
    public static final int ECategoryTypeAdvertisement = 2;
    public static final int ECategoryTypeAdvertisementURTube = 3;

    public @CategoryType  int      categoryType;
    public CategoryTypeEnum(@CategoryType int season) {
        categoryType = season;
        System.out.println("Season :" + season);
    }

    @IntDef({ECategoryTypePager, ECategoryTypeMedia, ECategoryTypeAdvertisement, ECategoryTypeAdvertisementURTube})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CategoryType {

    }
}
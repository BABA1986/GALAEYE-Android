package gala.com.kidstv.Refactored.DataHandlers.emums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CategoryTypeEnum {

    public static final int ECategoryTypePager = 0;
    public static final int ECategoryTypePosters = 1;
    public static final int ECategoryTypeThumbnail = 2;
    public static final int ECategoryTypeCircular = 3;
    public static final int ECategoryTypeCircularText = 4;
    public static final int ECategoryTypeAdvertisement = 5;
    public static final int ECategoryTypeAdvertisementURTube = 6;

    public @CategoryType  int      categoryType;
    public CategoryTypeEnum(@CategoryType int season) {
        categoryType = season;
        System.out.println("Season :" + season);
    }

    @IntDef({ECategoryTypePager, ECategoryTypePosters, ECategoryTypeThumbnail, ECategoryTypeCircular,ECategoryTypeCircularText, ECategoryTypeAdvertisement, ECategoryTypeAdvertisementURTube})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CategoryType {

    }
}
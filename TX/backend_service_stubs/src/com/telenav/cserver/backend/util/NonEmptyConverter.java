/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.util;

import com.telenav.billing2.common.dataTypes.NonEmptyBoolean;
import com.telenav.billing2.common.dataTypes.NonEmptyDouble;
import com.telenav.billing2.common.dataTypes.NonEmptyInt;
import com.telenav.billing2.common.dataTypes.NonEmptyLong;

/**
 * NonEmptyConverter
 * @author kwwang
 *
 */
public class NonEmptyConverter {

    public static Integer toInteger(NonEmptyInt nonEmpty) {
        return nonEmpty == null || nonEmpty.getIsNull() ? null : nonEmpty.getValue();
    }

    public static Long toLong(NonEmptyLong nonEmpty) {
        return nonEmpty == null || nonEmpty.getIsNull() ? null : nonEmpty.getValue();
    }

    public static Double toDouble(NonEmptyDouble nonEmpty) {
        return nonEmpty == null || nonEmpty.getIsNull() ? null : nonEmpty.getValue();
    }

    public static Boolean toBoolean(NonEmptyBoolean nonEmpty) {
        return nonEmpty == null || nonEmpty.getIsNull() ? null : nonEmpty.getValue();
    }

    public static NonEmptyInt toNonEmptyInteger(Integer value) {
        if (value == null) {
            return null;
        }
        NonEmptyInt nonEmpty = new NonEmptyInt();
        nonEmpty.setValue(value);
        return nonEmpty;
    }

    public static NonEmptyLong toNonEmptyLong(Long value) {
        if (value == null) {
            return null;
        }
        NonEmptyLong nonEmpty = new NonEmptyLong();
        nonEmpty.setValue(value);
        return nonEmpty;
    }

    public static NonEmptyDouble toNonEmptyDouble(Double value) {
        if (value == null) {
            return null;
        }
        NonEmptyDouble nonEmpty = new NonEmptyDouble();
        nonEmpty.setValue(value);
        return nonEmpty;
    }

    public static NonEmptyBoolean toNonEmptyBoolean(Boolean value) {
        if (value == null) {
            return null;
        }
        NonEmptyBoolean nonEmpty = new NonEmptyBoolean();
        nonEmpty.setValue(value);
        return nonEmpty;
    }
}

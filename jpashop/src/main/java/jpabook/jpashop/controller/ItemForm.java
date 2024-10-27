package jpabook.jpashop.controller;

import jpabook.jpashop.domian.AvailableStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {
    private Long id;
    private String name;
    private AvailableStatus isOrderable;
    private Boolean isIceAvailable;
    private Boolean isHotAvailable;
    private Long categoryId;
}
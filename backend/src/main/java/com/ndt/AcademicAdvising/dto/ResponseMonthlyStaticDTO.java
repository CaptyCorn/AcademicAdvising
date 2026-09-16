/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseMonthlyStaticDTO {
    private List<MonthlyStatisticDTO> usersByMonth;
    private List<MonthlyStatisticDTO> postsByMonth;
    private List<MonthlyStatisticDTO> booksByMonth;
    private List<MonthlyStatisticDTO> paymentsByMonth;
}

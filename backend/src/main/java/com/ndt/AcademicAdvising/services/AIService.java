/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;


/**
 *
 * @author ngodo
 */
public interface AIService {
    String getMessage(String question);
    String chat(int conversationId, String question);
}

package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.ChangeTickerAppointmentReson;

import java.util.List;
//FIXME
//TODO add annotation
public class GetChangeTicketAppointmentReasonsResult {

    public Integer statusCode;
    public String message;
    public List<ChangeTickerAppointmentReson> result;

    public GetChangeTicketAppointmentReasonsResult(Integer statusCode, String message, List<ChangeTickerAppointmentReson> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}

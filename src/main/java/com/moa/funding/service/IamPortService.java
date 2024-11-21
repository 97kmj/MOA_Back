package com.moa.funding.service;

public interface IamPortService {
	boolean verifyPayment(Long amount, String impUid);
}

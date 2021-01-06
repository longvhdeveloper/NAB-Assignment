package vn.co.abc.banking.voucher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.co.abc.banking.proto.GetVoucherRequest;
import vn.co.abc.banking.voucher.entity.Voucher;
import vn.co.abc.banking.voucher.entity.VoucherApiResponse;
import vn.co.abc.banking.voucher.repository.VoucherRepository;

import java.util.Optional;

@Service
@Slf4j
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final RestTemplate restTemplate;

    @Value("${external-api.url}")
    private String apiUrl;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, RestTemplate restTemplate) {
        this.voucherRepository = voucherRepository;
        this.restTemplate = restTemplate;
    }

    public Optional<Voucher> getVoucher(GetVoucherRequest request) {
        VoucherApiResponse voucherApiResponse = getDataFromExternal();
        Voucher voucher = new Voucher(voucherApiResponse.getCode(), request.getTransactionId(), request.getPhoneNumber());
        return Optional.of(voucherRepository.save(voucher));
    }

    private VoucherApiResponse getDataFromExternal() {
        return restTemplate.getForObject(apiUrl, VoucherApiResponse.class);
    }
}

package vn.co.abc.banking.voucher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.co.abc.banking.proto.GetVoucherRequest;
import vn.co.abc.banking.proto.GetVouchersRequest;
import vn.co.abc.banking.voucher.entity.Voucher;
import vn.co.abc.banking.voucher.entity.VoucherApiResponse;
import vn.co.abc.banking.voucher.repository.VoucherRepository;
import vn.co.abc.banking.voucher.specification.VoucherSpecification;

import java.util.LinkedList;
import java.util.List;
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
        if (voucherApiResponse.getCode().isEmpty()) {
            return Optional.empty();
        }

        log.info("Response API: {}", voucherApiResponse);

        Voucher voucher = new Voucher(voucherApiResponse.getCode(), request.getTransactionId(), request.getPhoneNumber());
        return Optional.of(voucherRepository.save(voucher));
    }

    public List<Voucher> getVouchers(GetVouchersRequest request) {
        List<Voucher> vouchers = new LinkedList<>();

        List<Specification<Voucher>> specifications = getSpecifications(request);

        if (specifications.isEmpty()) {
            voucherRepository.findAll().forEach(vouchers::add);
        } else {
            Specification<Voucher> specification = specifications.stream().reduce(Specification::and).get();
            vouchers.addAll(voucherRepository.findAll(Specification.where(specification)));
        }

        return vouchers;
    }

    private VoucherApiResponse getDataFromExternal() {
        return restTemplate.getForObject(apiUrl, VoucherApiResponse.class);
    }

    private List<Specification<Voucher>> getSpecifications(GetVouchersRequest request) {
        List<Specification<Voucher>> specifications = new LinkedList<>();

        if (!request.getPhoneNumber().isEmpty()) {
            specifications.add(VoucherSpecification.hasPhoneNumber(request.getPhoneNumber()));
        }

        return specifications;
    }
}

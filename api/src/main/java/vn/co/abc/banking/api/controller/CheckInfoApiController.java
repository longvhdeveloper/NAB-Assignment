package vn.co.abc.banking.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.co.abc.banking.api.controller.request.GetVouchersInfoRequest;
import vn.co.abc.banking.api.controller.request.PassCodeRequest;
import vn.co.abc.banking.api.controller.response.GetVouchersInfoResponse;
import vn.co.abc.banking.api.controller.response.PassCodeResponse;
import vn.co.abc.banking.api.controller.response.VoucherResponse;
import vn.co.abc.banking.api.entity.Passcode;
import vn.co.abc.banking.api.exception.PasscodeNotValidException;
import vn.co.abc.banking.api.service.CheckInfoService;
import vn.co.abc.banking.proto.VoucherInfo;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@Slf4j
public class CheckInfoApiController {

    private final CheckInfoService checkInfoService;

    @Autowired
    public CheckInfoApiController(CheckInfoService checkInfoService) {
        this.checkInfoService = checkInfoService;
    }

    @PostMapping("/passcode")
    public ResponseEntity<PassCodeResponse> getPasscode(@Valid @RequestBody PassCodeRequest request) {
        Passcode passcode = checkInfoService.getPasscode(request);
        return ResponseEntity.ok(PassCodeResponse.builder().passCode(passcode.getPassCode()).build());
    }

    @PostMapping("/vouchers")
    public ResponseEntity<GetVouchersInfoResponse> getVouchersInfo(@Valid @RequestBody GetVouchersInfoRequest request)
            throws PasscodeNotValidException {
        List<VoucherInfo> voucherInfoList = checkInfoService.getVouchersInfo(request);

        return ResponseEntity.ok(GetVouchersInfoResponse.builder()
                .vouchers(voucherInfoList.stream().map(voucherInfo -> VoucherResponse.builder()
                        .code(voucherInfo.getCode())
                        .build()).collect(Collectors.toList())).build());
    }
}

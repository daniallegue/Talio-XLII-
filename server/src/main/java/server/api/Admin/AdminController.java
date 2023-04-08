package server.api.Admin;

import commons.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private Map<Object, Consumer<String>> listeners = new HashMap<>();


    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Getter for the server password
     * @return Result
     */
    @GetMapping({"/get-password/"})
    public Result<String> getPassword(){
        return Result.SUCCESS.of(adminService.retrievePassword());
    }

    /**
     * @return Resukt
     */
    @GetMapping({"/reset/"})
    public Result<Object> resetPassword(){
        String password = adminService.resetPassword();
        listeners.forEach((k,v)->{
            v.accept(password);
        });
        return Result.SUCCESS;
    }

    /**
     * Maintains the long polling connection, propagates password changes to all clients
     * @return
     */
    @GetMapping({"/check-password-change/"})
    public DeferredResult<Result<String>> checkPasswordChange(){
        var noContent = Result.NO_CONTENT;
        var res = new DeferredResult<Result<String>>(5000L,noContent);
        var key = new Object();
        listeners.put(key, result->{
            res.setResult(Result.SUCCESS.of(result));
            System.out.println("Propagating password change using long-polling");
        });

        res.onCompletion(()->listeners.remove(key));
        return res;
    }


    /**
     * Getter for listeners , for testing purposes only
     *
     * @return int
     */
    public Map<Object, Consumer<String>> getListeners() {
        return listeners;
    }

    /** Setter for listeners, for testing purposes only
     *
     */
    public void setListeners(Map<Object, Consumer<String>> listenersMock) {
        this.listeners = listenersMock;
    }
}

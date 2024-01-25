package com.freeze.stock.controller;

//import com.freeze.stock.dto.StockDTO;
import com.freeze.stock.dto.CommentDTO;
import com.freeze.stock.dto.StockDTO;
import com.freeze.stock.service.CommentService;
import com.freeze.stock.service.StockService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/stock")
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private final StockService stockService;
    private final CommentService commentService;

//    @GetMapping("/save")
//    public String saveForm() {
//
//        return "save";
//    }
//
//    @PostMapping("/save")
//    public String save(@ModelAttribute StockDTO stockDTO) {
//        System.out.println("stockDTO = " + stockDTO);
//        stockService.save(stockDTO);
//        return "index";
//    }

//    @GetMapping("/save")
//    public String saveForm() {
//        // 현재는 HTML 페이지로 이동하는 기능이므로 유지합니다.
//        return "save";
//    }
//
//    @PostMapping("/save")
//    public void save(@RequestBody StockDTO stockDTO) {
//        // @RequestBody 어노테이션을 사용하여 JSON 데이터를 StockDTO 객체로 매핑합니다.
//        stockService.save(stockDTO);
//        // 여기서 리턴값을 정하지 않았는데, 재고를 추가한 후 어떤 응답을 할지 고려해야 합니다.
//        // 예를 들어, 성공적으로 추가되었을 경우 200 OK 응답을 보내거나,
//        // 실패했을 경우 400 Bad Request 등의 응답을 보낼 수 있습니다.
//    }

    private List<StockDTO> temporaryStockList = new ArrayList<>();

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody StockDTO stockDTO) {
        // 임시로 추가된 목록에 저장
        temporaryStockList.add(stockDTO);
        return ResponseEntity.ok("Stock added to temporary list.");
    }

    @PostMapping("/save-all")
    public ResponseEntity<String> saveAll(@RequestBody List<StockDTO> stockDTOList) {
        try {
            // 순회하여 각 StockDTO를 DB에 저장
            for (StockDTO stockDTO : stockDTOList) {
                stockService.save(stockDTO);
            }
            System.out.println("stockDTOList = " + stockDTOList);
            return ResponseEntity.ok("All stocks saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving stocks.");
        } finally {
            temporaryStockList.clear();  // 임시 목록 비우기
        }
    }


    @GetMapping
    public List<StockDTO> findAll() {
        return stockService.findAll();
    }

    // 데이터를 가져올때는 모델 객체를 사용한다
    @GetMapping("/")
    public String findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<StockDTO> stockDTOList = stockService.findAll();
        model.addAttribute("stockList", stockDTOList);
        return "list";
    }

//    @GetMapping("/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//        StockDTO stockDTO = stockService.findById(id);
//        /* 댓글 목록 가져오기 */
//        List<CommentDTO> commentDTOList = commentService.findAll(id);
//        model.addAttribute("commentList", commentDTOList);
//
//        model.addAttribute("stock", stockDTO);
//        return "detail";
//    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> findById(@PathVariable Long id) {
        StockDTO stockDTO = stockService.findById(id);
//        List<CommentDTO> commentDTOList = commentService.findAll(id);
//        stockDTO.setCommentList(commentDTOList);

        return ResponseEntity.ok(stockDTO);
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        StockDTO stockDTO = stockService.findById(id);
        model.addAttribute("stockUpdate", stockDTO);
        return "update";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute StockDTO stockDTO, Model model) {
        StockDTO stock = stockService.update(stockDTO);
        model.addAttribute("stock", stock);
        return "detail";
    }



    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.delete(id);
    }




//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        stockService.delete(id);
//        return "redirect:/stock/";
//    }
//
//    @PostMapping("/save")
//    public String save(@ModelAttribute StockDTO stockDTO) throws IOException {
//        System.out.println("stockDTO = " + stockDTO);
//        stockService.save(stockDTO);
//        return "index";
//    }
//
//    @GetMapping("/")
//    public String findAll(Model model) {
//        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
//        // 전체 데이터를 가져올 때는 model 객체를 사용하면 된다.
//        List<StockDTO> stockDTOList = stockService.findAll();
//        model.addAttribute("stockList", stockDTOList);
//        return "list";
//    }
}

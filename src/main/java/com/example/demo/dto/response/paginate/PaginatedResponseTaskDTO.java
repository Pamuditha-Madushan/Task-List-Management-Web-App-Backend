package com.example.demo.dto.response.paginate;

import com.example.demo.dto.SuperDTO;
import com.example.demo.dto.response.ResponseTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedResponseTaskDTO implements SuperDTO {

    private Long taskCount;
    private List<ResponseTaskDTO> taskDatalist;

}

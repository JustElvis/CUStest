package controller;

import dto.mapper.StudentMapper;
import dto.request.StudentRequestDto;
import dto.response.StudentResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import model.Student;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.StudentService;
import util.SortUtil;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final SortUtil sortUtil;

    @PostMapping
    public StudentResponseDto create(@RequestBody StudentRequestDto requestDto) {
        Student student = studentService.save(studentMapper.mapToModel(requestDto));
        return studentMapper.mapToDto(student);
    }

    @GetMapping
    public StudentResponseDto getByFirstNameAndLastName(@RequestParam String firstName,
                                                        @RequestParam String lastName) {
        Student student = studentService.getByFirstNameAndLastName(firstName, lastName);
        return studentMapper.mapToDto(student);
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Long id) {
        studentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateById(@PathVariable Long id,
                                         @RequestBody StudentRequestDto requestDto) {
        Student student = studentMapper.mapToModel(requestDto);
        student.setId(id);
        return studentMapper.mapToDto(studentService.save(student));
    }

    @GetMapping
    public List<StudentResponseDto> getAll(@RequestParam(defaultValue = "20") Integer count,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "firstName") String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, count, sortUtil.getSort(sortBy));
        return studentService.getAll(pageRequest)
                .stream()
                .map(studentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<StudentResponseDto> getAllByTeacherId(@RequestParam(defaultValue = "20") Integer count,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "firstName") String sortBy,
                                                    @RequestParam Long teacherId) {
        PageRequest pageRequest = PageRequest.of(page, count, sortUtil.getSort(sortBy));
        return studentService.getAllByTeacherId(teacherId, pageRequest)
                .stream()
                .map(studentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/attach/teacher")
    void attachTeacherToStudent(@PathVariable Long id,
                                @RequestParam Long teacherId) {
        studentService.attachTeacherToStudent(id, teacherId);
    }
}

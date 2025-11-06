package com.example.commerce_back_office.service;

import com.example.commerce_back_office.dto.user.UserDetailResponseDto;
import com.example.commerce_back_office.dto.user.UserRequestDto;
import com.example.commerce_back_office.dto.user.UserResponseDto;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.exception.NotFoundException;
import com.example.commerce_back_office.exception.code.ErrorCode;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.commerce_back_office.exception.code.ErrorCode.USER_NOT_FOUND;

/**
 * 유저 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 모든 유저를 조회하고 UserResponseDto 리스트로 반환합니다.
     *
     * @return List<UserResponseDto> 전체 유저 정보를 담은 DTO 리스트
     */
    public List<UserResponseDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID를 가진 유저를 조회하고 유저의 상세 정보를 반환합니다.
     *
     * @param id 조회할 유저의 ID
     * @return UserDetailResponseDto 해당 유저의 상세 정보 DTO
     * @throws IllegalArgumentException 해당 ID의 유저가 존재하지 않을 경우 발생
     */
    public UserDetailResponseDto getOne(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(USER_NOT_FOUND)
        );

        return UserDetailResponseDto.from(user);
    }

    /**
     * 주어진 ID에 해당하는 유저 정보를 찾아서, 요청된 필드(name, role)만 수정합니다.
     * 요청되지 않은 필드는 그대로 유지됩니다.
     *
     * @param id 수정할 유저의 ID
     * @param request 수정할 정보가 담긴 UserRequestDto 객체
     * @throws IllegalArgumentException 해당 ID의 유저가 존재하지 않을 경우
     * @return 수정된 유저 정보를 담은 UserDetailResponseDto
     */
    public UserDetailResponseDto patch(Long id,UserRequestDto request) {
        // 유저 id 존재 여부 확인
        User user = userRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(USER_NOT_FOUND)
        );
        // 유저 데이터 수정
        user.patchUser(request);

        // 데이터 저장
        User patchUser = userRepository.save(user);

        return UserDetailResponseDto.from(patchUser);
    }

    /**
     * 입력된 키워드를 name 또는 email 컬럼에 포함하고 있는 유저를 모두 조회합니다.
     * 대소문자 구분 없이 포함 여부를 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @return List<UserResponseDto> 키워드를 포함한 전체 유저 목록을 담은 DTO 리스트
     */
    public List<UserResponseDto> getAllByKeyword(String keyword) {

        // 이름 또는 이메일에서 키워드를 포함한 유저를 찾아서 반환
        List<User> userList = userRepository.searchKeyword(keyword);

        return userList.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }
}

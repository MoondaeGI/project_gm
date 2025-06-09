package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardDTO;
import com.example.gitmanager.board.dto.board.BoardInsertDTO;
import com.example.gitmanager.board.dto.board.BoardUpdateDTO;
import com.example.gitmanager.board.entity.board.Board;
import com.example.gitmanager.board.entity.board.BoardCategory;
import com.example.gitmanager.board.repository.board.BoardCategoryRepository;
import com.example.gitmanager.board.repository.board.BoardRepository;
import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.file.service.FileService;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Override
    public List<BoardDTO> findByProjectId(long projectId, int page) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, boardRepository.countByProject(project));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findByProject(project, pageRequest).getContent().stream()
                .map(BoardDTO::of)
                .toList();
    }

    @Override
    public List<BoardDTO> findByBoardCategoryId(long boardCategoryId, int page) {
        BoardCategory boardCategory = boardCategoryRepository.findById(boardCategoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", boardCategoryId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, boardRepository.countByBoardCategory(boardCategory));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findByBoardCategory(boardCategory, pageRequest).getContent().stream()
                .map(BoardDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public BoardDTO findById(long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글이 없습니다.", id)));
        board.increaseViewCount();

        return BoardDTO.of(board);
    }

    @Transactional
    @Override
    public long insert(BoardInsertDTO dto, String loginId) {
        BoardCategory boardCategory = boardCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", dto.getCategoryId())));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(boardCategory.getProject(), member);
        if (projectMember == null) {
            throw new UnAuthenticationException();
        }

        Board board = boardRepository.save(Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .boardCategory(boardCategory)
                .writer(projectMember)
                .build());

        if (dto.getMultipartFiles() != null) {
            FilesDTO filesDTO = FilesDTO.builder()
                    .mapperId(board.getId())
                    .mapperName("board")
                    .build();
            fileService.insert(dto.getMultipartFiles(), filesDTO);
        }

        return board.getId();
    }

    @Transactional
    @Override
    public long update(BoardUpdateDTO dto, String loginId) {
        Board board = boardRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글이 없습니다.", dto.getId())));

        BoardCategory boardCategory = boardCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", dto.getCategoryId())));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(boardCategory.getProject(), member);
        if (projectMember.getId() == board.getWriter().getId()) {
            board.update(dto, boardCategory);

            if (dto.getMultipartFiles() != null) {
                fileService.update(dto.getMultipartFiles(), dto.getFileDetailDTOList());
            }

            return board.getId();
        } else {
            throw new UnAuthenticationException();
        }
    }

    @Transactional
    @Override
    public void delete(long id, String loginId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글이 없습니다.", id)));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        if (board.getWriter().getMember().getId() == member.getId()) {
            FilesDTO filesDTO = FilesDTO.builder()
                    .mapperId(board.getId())
                    .mapperName("board")
                    .build();
            fileService.deleteAll(filesDTO);

            boardRepository.delete(board);
        } else {
            throw new UnAuthenticationException();
        }
    }
}

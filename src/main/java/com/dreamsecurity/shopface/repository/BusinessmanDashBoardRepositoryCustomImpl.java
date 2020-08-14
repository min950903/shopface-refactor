package com.dreamsecurity.shopface.repository;

import static com.dreamsecurity.shopface.domain.QSchedule.schedule;
import static com.dreamsecurity.shopface.domain.QEmploy.employ;
import static com.dreamsecurity.shopface.domain.QOccupation.occupation;
import static com.dreamsecurity.shopface.domain.QRecord.record;
import com.dreamsecurity.shopface.dto.dashboard.businessmanDashBoard.BusinessmanDashBoardListRequestDto;
import com.dreamsecurity.shopface.dto.dashboard.businessmanDashBoard.BusinessmanDashBoardListResponseDto;
import com.dreamsecurity.shopface.enums.ScheduleState;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class BusinessmanDashBoardRepositoryCustomImpl implements BusinessmanDashBoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BusinessmanDashBoardListResponseDto> getBusinessmanDashBoardListScheduled(BusinessmanDashBoardListRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                .select(Projections.constructor(
                        BusinessmanDashBoardListResponseDto.class,
                        employ.no, employ.name,
                        occupation.name,
                        schedule.workStartTime, schedule.workEndTime,
                        record.workingTime, record.quittingTime,
                        schedule.state
                ))
                .from(schedule)
                .leftJoin(employ).on(employ.member.id.eq(schedule.member.id)
                        .and(employ.branch.no.eq(schedule.branch.no)))
                .leftJoin(occupation).on(occupation.branch.no.eq(schedule.branch.no)
                        .and(occupation.no.eq(schedule.occupation.no)))
                .where(schedule.member.id.eq(requestDto.getMemberId())
                        .and(schedule.state.eq("R").or(schedule.state.eq("L")))
                        .and(schedule.branch.no.eq(requestDto.getBranchNo()))
                        .and(schedule.workStartTime.after(now)))
                .fetch();
    }

    @Override
    public List<BusinessmanDashBoardListResponseDto> getBusinessmanDashBoardListWorking(BusinessmanDashBoardListRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();

        return queryFactory
                .select(Projections.constructor(
                        BusinessmanDashBoardListResponseDto.class,
                        employ.no, employ.name,
                        occupation.name,
                        schedule.workStartTime, schedule.workEndTime,
                        record.workingTime, record.quittingTime,
                        schedule.state
                ))
                .from(schedule)
                .leftJoin(employ).on(employ.member.id.eq(schedule.member.id)
                        .and(employ.branch.no.eq(schedule.branch.no)))
                .leftJoin(occupation).on(occupation.branch.no.eq(schedule.branch.no)
                        .and(occupation.no.eq(schedule.occupation.no)))
                .leftJoin(record).on(record.branchNo.eq(schedule.branch.no)
                        .and(record.memberId.eq(schedule.member.id))
                        .and(record.workStartTime.eq(schedule.workStartTime))
                        .and(record.workEndTime.eq(schedule.workEndTime)))
                .where(schedule.member.id.eq(requestDto.getMemberId())
                        .and(schedule.state.eq("W"))
                        .and(schedule.branch.no.eq(requestDto.getBranchNo()))
                        .and(schedule.workStartTime.before(now)))
                .fetch();
    }

    @Override
    public List<BusinessmanDashBoardListResponseDto> getBusinessmanDashBoardListWorkDone(BusinessmanDashBoardListRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();

        return queryFactory
                .select(Projections.constructor(
                        BusinessmanDashBoardListResponseDto.class,
                        employ.no, employ.name,
                        record.occupationName,
                        schedule.workStartTime, schedule.workEndTime,
                        record.workingTime, record.quittingTime,
                        schedule.state
                ))
                .from(record)
                .leftJoin(employ).on(employ.member.id.eq(record.memberId)
                        .and(employ.branch.no.eq(record.branchNo)))
                .leftJoin(schedule).on(record.branchNo.eq(schedule.branch.no)
                        .and(record.memberId.eq(schedule.member.id))
                        .and(record.workStartTime.eq(schedule.workStartTime))
                        .and(record.workEndTime.eq(schedule.workEndTime)))
                .where(record.memberId.eq(requestDto.getMemberId())
                        .and(record.branchNo.eq(requestDto.getBranchNo()))
                        .and(record.workEndTime.before(now)))
                .fetch();
    }
}


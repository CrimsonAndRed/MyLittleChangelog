package my.little.changelog.service.group

import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import my.little.changelog.BaseMockedDbTest
import my.little.changelog.model.exception.VersionIsNotLatestException
import my.little.changelog.model.group.dto.external.GroupDeletionDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

@KtorExperimentalAPI
internal class GroupServiceTest : BaseMockedDbTest() {

    init {
        mockkObject(GroupRepo)
        mockkObject(VersionRepo)
        mockkObject(LeafRepo)
    }

    @Test
    fun `Test Create New Root Group Success`() {
        val version = createVersion(0)
        val dto = GroupCreationDto(
            name = "Тестовая группа",
            versionId = version.id.value,
        )
        val createdGroup = createGroup(0, 0, version, dto.name, null)

        every { VersionRepo.findLatest() } returns version
        every { VersionRepo.findById(version.id.value) } returns version
        every { GroupRepo.create(allAny()) } returns createdGroup

        val res = GroupService.createGroup(dto)

        assertEquals(createdGroup.id.value, res.id)
        assertEquals(createdGroup.vid, res.vid)
        assertEquals(createdGroup.name, res.name)
        assertEquals(createdGroup.parentVid, res.parent?.vid)
    }

    @Test
    fun `Test Create New Sub Group Success`() {
        val version = createVersion(0)
        val rootGroup = createGroup(0, 0, version, "Рутовая группа", null)
        val dto = GroupCreationDto(
            name = "Тестовая саб группа",
            versionId = version.id.value,
            parentId = rootGroup.id.value
        )
        val createdGroup = createGroup(1, 1, version, dto.name, rootGroup.vid)

        every { VersionRepo.findLatest() } returns version
        every { VersionRepo.findById(version.id.value) } returns version
        every { GroupRepo.findById(rootGroup.id.value) } returns rootGroup
        every { GroupRepo.create(allAny()) } returns createdGroup

        val res = GroupService.createGroup(dto)

        assertEquals(createdGroup.id.value, res.id)
        assertEquals(createdGroup.vid, res.vid)
        assertEquals(createdGroup.name, res.name)
        assertEquals(createdGroup.parentVid, res.parent?.vid)
    }

    @Test
    fun `Test Create Group in New Version Success`() {
        val version1 = createVersion(0)
        val group = createGroup(0, 0, version1,"Тестовая группа", null)
        val version2 = createVersion(1)

        val dto = GroupCreationDto(
            name = group.name,
            vid = group.vid,
            versionId = version2.id.value
        )
        val createdGroup = createGroup(1, dto.vid!!, version2, dto.name, null)

        every { VersionRepo.findLatest() } returns version2
        every { VersionRepo.findById(version1.id.value) } returns version1
        every { VersionRepo.findById(version2.id.value) } returns version2
        every { GroupRepo.findById(group.id.value) } returns group
        every { GroupRepo.create(allAny()) } returns createdGroup

        val res = GroupService.createGroup(dto)

        assertEquals(createdGroup.id.value, res.id)
        assertEquals(createdGroup.vid, res.vid)
        assertEquals(createdGroup.name, res.name)
        assertEquals(createdGroup.parentVid, res.parent?.vid)
    }

    @Test
    fun `Test Create Group Failure`() {
        val version1 = createVersion(0)
        val group = createGroup(0, 0, version1, "Тестовая группа", null)
        val version2 = createVersion(1)
        val dto = GroupCreationDto(
            name = group.name,
            versionId = version1.id.value
        )

        every { VersionRepo.findLatest() } returns version2
        every { VersionRepo.findById(version1.id.value) } returns version1
        every { VersionRepo.findById(version2.id.value) } returns version2
        every { GroupRepo.findById(group.id.value) } returns group

        assertThrows<VersionIsNotLatestException> { GroupService.createGroup(dto) }
    }

    // TODO(#1) В сервисе мутируется Group, на этом мок ломается
    // @Test
    // fun `Test Update Group Without Parent Update Success`() {
    //     val version = createVersion(0)
    //     val group = createGroup(0, 0, version,"Тестовая группа", null)
    //
    //     val dto = GroupUpdateDto(group.id.value, "${group.name}+NewValue", group.parentVid)
    //     val mockGroup = createGroup(0, 0, )
    //
    //     every { VersionRepo.findById(version.id.value) } returns version
    //     every { GroupRepo.findById(group.id.value) } returns group
    //     every { GroupRepo.update(allAny()) } returns
    //
    //     val res = GroupService.updateGroup(dto)
    // }

    // TODO(#2) В group нельзя обратиться к version
//    @Test
//    fun `Test Group Deletion Success`() {
//        val version1 = createVersion(0)
//        val group = createGroup(0, 0, version1,"Тестовая группа", null)
//
//        val dto = GroupDeletionDto(
//            id = group.id.value
//        )
//
//        every { GroupRepo.findById(allAny()) } returns group
//        every { VersionRepo.findLatest() } returns version1
//        every { LeafRepo.findCurrentGroupLeaves(allAny()) } returns emptyList()
//        every { GroupRepo.findSubgroups(allAny()) } returns emptyList()
//
//        assertDoesNotThrow {
//            GroupService.deleteGroup(dto)
//        }
//    }

}
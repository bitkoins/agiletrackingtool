class ProjectService {

    boolean transactional = true
    def itemGroupService

    def delete(def project) {
    	PointsSnapShot.findAllByProject(project).each{ it.delete() }
		Iteration.findAllByProject(project).each{ it.unloadItemsAndDelete() }
		ItemGroup.findAllByProject(project).each{ group -> itemGroupService.deleteWholeGroup(group) }
		project.delete()
    }
}

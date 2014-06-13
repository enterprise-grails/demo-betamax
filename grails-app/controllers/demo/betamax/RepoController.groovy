package demo.betamax

class RepoController {

    def repoService

    def index() {
        def repos = repoService.list()
        [repos: repos ?: [:]]
    }
}
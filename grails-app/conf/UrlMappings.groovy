import rummygame.GameDetailsController

class UrlMappings {

	static mappings = {
        "/gameDetails/index/$id" (controller: GameDetailsController ) {

        }
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/"(controller: 'home', action: 'index')
        "500"(view:'/error')
	}
}

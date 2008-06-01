import torricelli.Group

L l = taglib(L)

List<Group> groups = my.listGroups();

l.layout {
    l.left {
        H2("Repository Groups")

        DIV(CLASS:"repositoryList") {
            groups.each { g ->
                DIV {
                    A(HREF:g.name) {
                        IMG(SRC:"img/48x48/folder.gif",ALIGN:"middle")
                    }
                    A(HREF:g.name,g.name)
                    // just for now
                    text(" ")
                    A(HREF:g.name+'/configure',STYLE:"font-size:0.5em; margin-left:1em","(configure)")
                }
            }
        }
    }

    l.right {
        DIV(CLASS:"box",STYLE:"width:auto;") {
            H2("Create a new group")
            form("createGroup") {
                TR {
                    TD("Name")
                    TD { INPUT(TYPE:"text",NAME:"name") }
                }
            }
        }
    }
}

/**
 * Defines a form in the box.
 */
def form(String name,Closure body) {
    FORM(ACTION:name,METHOD:"post") {
        TABLE(body)
        DIV(ALIGN:"right") {
            INPUT(TYPE:"submit",VALUE:name)
        }
    }
}

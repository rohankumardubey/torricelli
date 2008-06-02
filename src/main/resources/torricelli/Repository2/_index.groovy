import org.kohsuke.scotland.dir.DirectoryTags;
import torricelli.DirectoryModelImpl;

L l = taglib(L)
DirectoryTags dt = taglib(DirectoryTags);

l.layout(title:my.name) {
    DIV(ID:"center",STYLE:"text-align:center; margin:1em;") {
        raw("Check out this repository by: <tt style='margin-left:1em'>hg clone ${request.requestURL}</tt>")
    }

    l.left {
        H2("Recent Changes")
        my.parse("/changelog").changelog.each { e ->
            DIV(CLASS:"changelog") {
                l.rev(e.@rev)
                text(" by ")
                l.author(e.@author)
                text(" ${e.@date} (${e.@age} ago)")

                e.tag.each { t ->
                    text(' ')
                    SPAN(CLASS:"csTag", t.text())
                }

                DIV(CLASS:"comment",e.description.text())

                DIV(CLASS:"files") {
                    e.file.each { f ->
                        DIV(CLASS:"file") {
                            A(HREF:"rev/${e.@rev}/diff/${f.text()}",f.text())
                        }
                    }
                }
            }
        }


        H2("Files")
        TABLE(ID:"files") {
            my.parse("/filesummary/?path=/").file.each { f ->
                TR {
                    TD { IMG(SRC:"${request.contextPath}/img/16x16/text.gif") }
                    TD(f.@name)
                    TD { l.rev(f.@rev) }
                    TD { l.author(f.author.text()) }
                }
                TR(CLASS:"comment") {
                    TD()
                    TD(COLSPAN:3, f.summary.text())
                }
            }
        }
    }

    l.right {
        l.nav([
            [HREF:"configure",  TITLE:"Configure"],
            [HREF:"tags",       TITLE:"Tags"]
        ])

        DIV(CLASS:"box") {
            H2("Directories")
            DIV(CLASS:"dirtree") {
                dt.list('.',DirectoryModelImpl.INSTANCE,my.getRev("tip").dirTree())
            }
        }
    }
}
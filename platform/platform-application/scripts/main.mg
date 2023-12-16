class Shell{
    fn test() : Shell{
        println("awa")
        return Shell
    }
    fn awa(){
        println("a")
        return Shell
    }
}

fn main(){
    Shell.test().awa()
}

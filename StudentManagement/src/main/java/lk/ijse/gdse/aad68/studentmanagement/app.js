$(document).ready(function (){
    $("#btn-submit").click






  const studentData = {
        name:nameF,
        email:emailF,
        level:levelF
  }

    //create JSON
    const studentJson = JSON.stringify(studentData)
    console.log(studentJson)

    //Introduce AJAX
    const http =new XMLHttpRequest()
    http.onreadystatechange = () => {
        if (http.readyState == 4){
            if(http.status == 200){
                var responseTextJson=JSON.stringify(http.responseText)
            }else {
                console.error("Failed")
                console.error("Status" + http.status)
                console.error("Ready State " + http.readyState)
            }
        }else {
            console.error("Ready State " + http.readyState)
        }

    }
    http.open("POST","http://localhost:8080/StudentManagement/student",true)
    http.setRequestHeader("Content-Type","application/json")
    http.send(studentData)
});
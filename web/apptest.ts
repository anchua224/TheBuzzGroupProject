var describe: any;
var it: any;
var expect: any;

describe("Tests of basic math functions", function() {

    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });
    it("UI Test: Add Button Hides Listing", function(){
        // click the button for showing the add button
        (<HTMLElement>document.getElementById("showFormButton")).click();
        // expect that the add form is not hidden
        expect( (<HTMLElement>document.getElementById("addElement")).style.display ).toEqual("block");
        // expect that the element listing is hidden
        expect( (<HTMLElement>document.getElementById("showElements")).style.display ).toEqual("none");
        // reset the UI, so we don't mess up the next test
        (<HTMLElement>document.getElementById("addCancel")).click();

    });

    it("UI Test: Edit Button", function(){
        // click the button for showing the edit button
        (<HTMLElement>document.getElementById("showFormButton")).click();
        // expect that the edit form is not hidden
        expect( (<HTMLElement>document.getElementById("editElement")).style.display ).toEqual("block");
        // expect that the element listing is hidden
        expect( (<HTMLElement>document.getElementById("showElements")).style.display ).toEqual("none");
        // reset the UI, so we don't mess up the next test
        (<HTMLElement>document.getElementById("editCancel")).click();

    });

    // it("Multiplication should work", function(){
    //     let a = 23;
    //     let b = 52;
    //     let mult = a * b;
    //     expect(mult).toEqual(1196);
    // });



});
const wrapper = document.querySelector('.student-post-list')
const btnright = document.getElementById('btnright');
const btnleft = document.getElementById('btnleft');

let isMouseDown = false
let startX, scrollLeft

wrapper.addEventListener('mousedown', (e) => {
	isMouseDown = true
	startX = e.pageX - wrapper.offsetLeft
	scrollLeft = wrapper.scrollLeft

	console.log(startX)
	console.log(wrapper.offsetLeft)
	console.log('e.pagx=',e.pageX)
	console.log('ws',wrapper.scrollLeft)
})

wrapper.addEventListener('mouseleave', () => {
	isMouseDown = false
})

wrapper.addEventListener('mouseup', () => {
	isMouseDown = false
})

wrapper.addEventListener('mousemove', (e) => {
	if (!isMouseDown) return

	const x = e.pageX - wrapper.offsetLeft
	// 3 là tốc độ scroll
	const walk = (x - startX) * 2
	wrapper.scrollLeft = scrollLeft - walk

	
	console.log('x=',x)
	console.log('walk=',walk)
})

var i = 1;

btnright.addEventListener('click', (e)=>{
	const walkx =  wrapper.offsetLeft * i
    wrapper.scrollLeft = scrollLeft + walkx
	i++
})


btnleft.addEventListener('click', ()=>{

})

